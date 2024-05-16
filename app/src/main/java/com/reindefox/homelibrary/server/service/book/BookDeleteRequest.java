package com.reindefox.homelibrary.server.service.book;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookDeleteRequest implements Serializable {
    @SerializedName("id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
