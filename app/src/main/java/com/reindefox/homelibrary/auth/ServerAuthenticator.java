package com.reindefox.homelibrary.auth;

public interface ServerAuthenticator {
    public String signUp(final String email, final String username, final String password);

    public String signIn(final String email, final String password);
}
