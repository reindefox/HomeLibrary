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
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.reindefox.homelibrary.AuthorizationCredentialsProvider;
import com.reindefox.homelibrary.databinding.ActivityAuthorizationBinding;

/**
 * Класс активности авторизации пользователя
 */
public class AuthorizationActivity extends AppCompatActivity {

    /**
     * Биндинг элемента
     */
    private ActivityAuthorizationBinding binding;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private static final String LOGIN = "login";

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
        binding = ActivityAuthorizationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = getSharedPreferences("user", MODE_PRIVATE)
                .edit();

        setupFieldLimitations();
        setupSignUpText();

        binding.login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString(LOGIN, s.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    /**
     * Установка ограничений полей данных
     */
    private void setupFieldLimitations() {
        binding.login.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(AuthorizationCredentialsProvider.MAX_USER_DATA_LENGTH)
        });

        binding.password.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(AuthorizationCredentialsProvider.MAX_USER_DATA_LENGTH)
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.login.setText(sharedPreferences.getString(LOGIN, null));
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

        // TODO проверить
//        finish();
    }

    private boolean loginUser() {


        return false;
    }
}