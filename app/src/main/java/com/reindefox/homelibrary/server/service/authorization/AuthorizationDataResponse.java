package com.reindefox.homelibrary.server.service.authorization;

import com.google.gson.annotations.SerializedName;

public class AuthorizationDataResponse {
    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("auth_token")
    private String authToken;

    public int getStatusCode() {
        return statusCode;
    }

    public String getAuthToken() {
        return authToken;
    }
}
