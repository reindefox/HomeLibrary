package com.reindefox.homelibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.databinding.ActivityAuthorizationBinding;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationDataRequest;
import com.reindefox.homelibrary.server.service.authorization.SignInResponse;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Класс активности авторизации пользователя
 */
public class AuthorizationActivity extends AbstractAuthActivity {

    /**
     * Биндинг элемента
     */
    private ActivityAuthorizationBinding binding;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    public static final String spLogin = "login_cache";

    /**
     * Базовая инициализация компонента
     *
     * @param savedInstanceState Если действие повторно инициализируется после предыдущего закрытия,
     *                           то этот пакет содержит данные,
     *                           которые оно последним предоставило в {@link #onSaveInstanceState}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthorizationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences(AuthorizationUtils.PREFS_USER, Context.MODE_PRIVATE);
        editor = getSharedPreferences(AuthorizationUtils.PREFS_USER, MODE_PRIVATE)
                .edit();

        setupFieldLimitations();
        setupSignUpText();

        // Сохранение логина в SP
        binding.login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString(spLogin, s.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.login.getText() == null || binding.password.getText() == null ||
                        binding.login.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()) {
                    Snackbar.make(view, R.string.login_empty, Snackbar.LENGTH_SHORT)
                            .show();

                    return;
                }

                attemptLoginUser();
            }
        });

        binding.testAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.login.setText("qq");
                binding.password.setText("qq");

                attemptLoginUser();
            }
        });

        // Автоматическая авторизация при сохраненных параметрах
        if (sharedPreferences.getBoolean(AuthorizationUtils.PREFS_AUTO_LOGIN, false)) {
            binding.login.setText(sharedPreferences.getString(ARG_USER_LOGIN, null));
            binding.password.setText(sharedPreferences.getString(ARG_USER_PWD, null));

            attemptLoginUser();
        }
    }

    /**
     * Установка ограничений полей данных
     */
    private void setupFieldLimitations() {
        binding.login.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(AuthorizationUtils.MAX_USER_DATA_LENGTH)
        });

        binding.password.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(AuthorizationUtils.MAX_USER_DATA_LENGTH)
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Восстановление данных из SP
        binding.login.setText(sharedPreferences.getString(spLogin, null));
    }

    /**
     * Создание кнопки для перехода на активность регистрации
     */
    private void setupSignUpText() {
        SpannableString spannableString = new SpannableString(binding.signUpText.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                redirectToSignUp();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
            }
        };

        spannableString.setSpan(clickableSpan, 20, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.signUpText.setText(spannableString);
        binding.signUpText.setMovementMethod(LinkMovementMethod.getInstance());
        binding.signUpText.setHighlightColor(Color.TRANSPARENT);
    }

    /**
     * Переход на активность регистрации
     */
    private void redirectToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);

        startActivity(intent);
    }

    private void redirectToApplication() {
        Intent intent = new Intent(this, ApplicationActivity.class);

        startActivity(intent);

        finish();
    }

    private void attemptLoginUser() {
        assert binding.login.getText() != null;
        assert binding.password.getText() != null;

        String login = binding.login.getText().toString();

        String passwordHash = AuthorizationUtils.applySHA256(binding.password.getText().toString());

        AuthorizationDataRequest authorizationDataRequest = new AuthorizationDataRequest();
        authorizationDataRequest.setUsername(login);
        authorizationDataRequest.setPassword(passwordHash);

        setDataInputWhenLoading(false);

        authorizationService.login(authorizationDataRequest).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_OK: {
                        if (response.body() == null) {
                            Snackbar.make(AuthorizationActivity.this.getWindow().getDecorView(), R.string.error, Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        editor.putString(ARG_USER_LOGIN, login);
                        editor.putString(ARG_USER_PWD, binding.password.getText().toString());
                        editor.putString(ARG_USER_ROLE, response.body().getRole());
                        editor.putString(ARG_AUTH_TOKEN_TYPE, response.body().getToken());
                        editor.apply();

                        redirectToApplication();
                        finish();
                        break;
                    }
                    case HttpURLConnection.HTTP_FORBIDDEN: {
                        setDataInputWhenLoading(true);

                        Snackbar.make(AuthorizationActivity.this.getWindow().getDecorView(), R.string.login_signup_wrong_pwd, Snackbar.LENGTH_SHORT)
                                .show();
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable throwable) {
                setDataInputWhenLoading(true);

                Snackbar.make(AuthorizationActivity.this.getWindow().getDecorView(), R.string.error, Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void setDataInputWhenLoading(boolean flag) {
        binding.signInButton.setEnabled(flag);
        binding.login.setEnabled(flag);
        binding.password.setEnabled(flag);
    }
}