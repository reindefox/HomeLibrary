package com.reindefox.homelibrary.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.snackbar.Snackbar;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.databinding.ActivitySignUpBinding;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationDataRequest;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationDataResponse;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationService;

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
                assert binding.password.getText() != null;
                assert binding.repeatPassword.getText() != null;

                if (!binding.password.getText().toString().equals(binding.repeatPassword.getText().toString())) {
                    Snackbar.make(view, R.string.login_signup_compare, Snackbar.LENGTH_SHORT)
                            .show();

                    return;
                }

                signUp();
            }
        });
    }

    private void signUp() {
        assert binding.login.getText() != null;
        assert binding.password.getText() != null;

        String login = binding.login.getText().toString();

        // Сразу хешируем пароль для предотвращения дальнейших утечек
        String passwordHash = AuthorizationUtils.applySHA256(binding.password.getText().toString());

        AuthorizationDataRequest authorizationDataRequest = new AuthorizationDataRequest();
        authorizationDataRequest.setLogin(login);
        authorizationDataRequest.setPassword(passwordHash);

        authorizationService.signUp(new AuthorizationDataRequest()).enqueue(new Callback<AuthorizationDataResponse>() {
            @Override
            public void onResponse(Call<AuthorizationDataResponse> call, Response<AuthorizationDataResponse> response) {

            }

            @Override
            public void onFailure(Call<AuthorizationDataResponse> call, Throwable throwable) {

            }
        });
    }
}