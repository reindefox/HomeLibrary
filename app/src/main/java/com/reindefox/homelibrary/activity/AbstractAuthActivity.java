package com.reindefox.homelibrary.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationService;

/**
 * Абстрактный класс для авторизации и регистрации
 */
public abstract class AbstractAuthActivity extends AppCompatActivity {
    public static final String ARG_AUTH_TOKEN_TYPE = "token";

    public static final String ARG_USER_LOGIN = "login";

    public static final String ARG_USER_PWD = "password";

    public static final String ARG_USER_ROLE = "role";

    /**
     * Разрешены только строчные, заглавные буквы, цифры и . _
     */
    public static final String ALLOWED_LOGIN_CHARS = "^[a-zA-Z0-9._]+$";

    /**
     * Разрешены только строчные, заглавные буквы, цифры и . _
     */
    public static final String ALLOWED_PWD_CHARS = "^[a-zA-Z0-9._]+$";

    /**
     * Максимальная длина имени пользователя и пароля
     */
    public static final int MAX_USER_DATA_LENGTH = 32;

    protected AuthorizationService authorizationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authorizationService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(AuthorizationService.class);
    }
}
