package com.reindefox.homelibrary.auth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ServerAuthenticatorImpl implements ServerAuthenticator {
    private static Map<String, String> mCredentialsRepo;

    @Override
    public String signUp(String email, String username, String password) {
        // TODO: register new user on the server and return its auth token
        return null;
    }

    @Override
    public String signIn(String email, String password) {
        String authToken = null;
        final DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");

        if (mCredentialsRepo.containsKey(email)) {
            if (password.equals(mCredentialsRepo.get(email))) {
                authToken = email + "-" + df.format(new Date());
            }
        }

        return authToken;
    }
}
