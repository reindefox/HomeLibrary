package com.reindefox.homelibrary.server.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("login")
    private String login;
}
