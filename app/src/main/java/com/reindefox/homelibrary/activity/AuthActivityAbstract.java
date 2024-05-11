package com.reindefox.homelibrary.activity;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.service.authorization.AuthorizationService;

public abstract class AuthActivityAbstract extends AppCompatActivity {
    public static final String ARG_ACCOUNT_TYPE = "accountType";

    public static final String ARG_AUTH_TOKEN_TYPE = "authTokenType";

    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "isAddingNewAccount";

    public static final String PARAM_USER_PASSWORD = "password";

    protected AccountManager accountManager;

    protected AuthorizationService authorizationService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        accountManager = AccountManager.get(this);

        authorizationService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(AuthorizationService.class);
    }

    protected boolean verifyInputData(TextInputEditText... textInputEditTexts) {
        // TODO
        for (TextInputEditText textInputEditText : textInputEditTexts) {
        }

        return true;
    }
}
