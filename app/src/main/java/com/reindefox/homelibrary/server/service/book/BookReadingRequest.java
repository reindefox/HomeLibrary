package com.reindefox.homelibrary.server.service.book;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookReadingRequest implements Serializable {
    @SerializedName("book_id")
    private Integer bookId;

    @SerializedName("state")
    private boolean state;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
