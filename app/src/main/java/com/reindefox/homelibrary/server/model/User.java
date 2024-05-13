package com.reindefox.homelibrary.server.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("login")
    private String login;

    @SerializedName("token")
    private String token;
}
