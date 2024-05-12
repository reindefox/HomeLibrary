package com.reindefox.homelibrary.activity;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationService;

public abstract class AuthActivityAbstract extends AppCompatActivity {
    public static final String ARG_ACCOUNT_TYPE = "user";

    public static final String ARG_AUTH_TOKEN_TYPE = "token";

    protected AccountManager accountManager;

    protected AuthorizationService authorizationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountManager = AccountManager.get(this);

        authorizationService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(AuthorizationService.class);
    }
}
