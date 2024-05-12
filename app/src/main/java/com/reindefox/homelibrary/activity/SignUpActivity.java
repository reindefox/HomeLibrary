package com.reindefox.homelibrary.activity;

import static com.reindefox.homelibrary.auth.AuthorizationUtils.MAX_USER_DATA_LENGTH;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.snackbar.Snackbar;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.auth.AccountUtils;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.databinding.ActivitySignUpBinding;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationDataRequest;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationDataResponse;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationService;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Класс активности регистрации пользователя
 */
public class SignUpActivity extends AuthActivityAbstract {

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

        // TODO finish()
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

    private void signUp(String login, String password) {
        // Сразу хешируем пароль для предотвращения дальнейших утечек
        String passwordHash = AuthorizationUtils.applySHA256(binding.password.getText().toString());

        AuthorizationDataRequest authorizationDataRequest = new AuthorizationDataRequest();
        authorizationDataRequest.setUsername(login);
        authorizationDataRequest.setPassword(passwordHash);

        authorizationService.signUp(authorizationDataRequest).enqueue(new Callback<AuthorizationDataResponse>() {
            @Override
            public void onResponse(Call<AuthorizationDataResponse> call, Response<AuthorizationDataResponse> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_CONFLICT: {
                        Snackbar.make(binding.getRoot().getRootView(), getString(R.string.login_signup_login_conflict), Snackbar.LENGTH_SHORT)
                                .show();

                        break;
                    }
                    case HttpURLConnection.HTTP_OK: {
                        Account account = new Account(login, AccountUtils.ACCOUNT_TYPE);

                        accountManager.setAuthToken(account, AccountUtils.ACCOUNT_TYPE, response.body().getAuthToken());
                        break;
                    }
                }

//                Account account = new Account(AccountUtils.ACCOUNT_TYPE, ARG_ACCOUNT_TYPE);
//
//                Log.i("123", response.toString());
////                accountManager.setAuthToken(account, ARG_AUTH_TOKEN_TYPE, response.body().getAuthToken());
//
//                Log.i("123", accountManager.peekAuthToken(account, ARG_AUTH_TOKEN_TYPE));
            }

            @Override
            public void onFailure(Call<AuthorizationDataResponse> call, Throwable throwable) {

            }
        });
    }
}