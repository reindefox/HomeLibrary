package com.reindefox.homelibrary.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationService;

public abstract class AuthActivityAbstract extends AppCompatActivity {
    public static final String ARG_AUTH_TOKEN_TYPE = "token";

    public static final String ARG_USER_LOGIN = "login";

    public static final String ARG_USER_ROLE = "role";

    protected AuthorizationService authorizationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authorizationService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(AuthorizationService.class);
    }
}
