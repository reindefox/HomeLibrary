package com.reindefox.homelibrary.activity;

import static com.reindefox.homelibrary.auth.AuthorizationUtils.MAX_USER_DATA_LENGTH;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.snackbar.Snackbar;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.databinding.ActivitySignUpBinding;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationDataRequest;
import com.reindefox.homelibrary.server.service.authorization.SignInResponse;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Класс активности регистрации пользователя
 */
public class SignUpActivity extends AbstractAuthActivity {

    /**
     * Биндинг элемента
     */
    private ActivitySignUpBinding binding;

    /**
     * Разрешить регистрацию без принятия соглашения
     */
    private static boolean ENABLE_WITHOUT_EULA = false;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    /**
     * Базовая инициализация компонента
     *
     * @param savedInstanceState Если действие повторно инициализируется после предыдущего закрытия,
     *                          то этот пакет содержит данные,
     *                          которые оно последним предоставило в {@link #onSaveInstanceState}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.signUpButton.setEnabled(ENABLE_WITHOUT_EULA);

        binding.checkBoxEULA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.signUpButton.setEnabled(isChecked);
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert binding.login.getText() != null;
                assert binding.password.getText() != null;
                assert binding.repeatPassword.getText() != null;

                String login = binding.login.getText().toString();
                String password = binding.password.getText().toString();
                String repeatPassword = binding.repeatPassword.getText().toString();

                if (login.isEmpty()) {
                    Snackbar.make(view, getString(R.string.login_signup_enter_login), Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (password.isEmpty()) {
                    Snackbar.make(view, getString(R.string.login_signup_enter_pwd), Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (repeatPassword.isEmpty()) {
                    Snackbar.make(view, getString(R.string.login_signup_enter_pwd_r), Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (login.length() > MAX_USER_DATA_LENGTH
                        || password.length() > MAX_USER_DATA_LENGTH) {
                    Snackbar.make(view, getString(R.string.login_signup_max_l) + MAX_USER_DATA_LENGTH, Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (!binding.password.getText().toString().equals(binding.repeatPassword.getText().toString())) {
                    Snackbar.make(view, R.string.login_signup_compare, Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                signUp(login, password);
            }
        });
    }

    /**
     * Регистрация пользователя в системе
     * @param login логин
     * @param password пароль
     */
    private void signUp(String login, String password) {
        String passwordHash = AuthorizationUtils.applySHA256(binding.password.getText().toString());

        AuthorizationDataRequest authorizationDataRequest = new AuthorizationDataRequest();
        authorizationDataRequest.setUsername(login);
        authorizationDataRequest.setPassword(passwordHash);

        authorizationService.signUp(authorizationDataRequest).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_CONFLICT: {
                        Snackbar.make(binding.getRoot().getRootView(), getString(R.string.login_signup_login_conflict), Snackbar.LENGTH_SHORT)
                                .show();

                        break;
                    }
                    case HttpURLConnection.HTTP_CREATED: {
                        Snackbar.make(binding.getRoot().getRootView(), getString(R.string.login_signup_created), Snackbar.LENGTH_SHORT)
                                .show();

                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable throwable) {

            }
        });
    }
}