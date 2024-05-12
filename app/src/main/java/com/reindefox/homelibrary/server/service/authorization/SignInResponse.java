package com.reindefox.homelibrary.server.service.authorization;

import com.google.gson.annotations.SerializedName;

public class SignInResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("login")
    private String login;

    @SerializedName("role")
    private String role;

    public String getToken() {
        return token;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }
}
