package com.reindefox.homelibrary.server.service.book;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookCreateRequest implements Serializable {
    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("description")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
