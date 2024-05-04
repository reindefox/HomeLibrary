package com.reindefox.homelibrary.server.service.connection;

import com.google.gson.annotations.SerializedName;

public class TestConnectionData {
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }
}
