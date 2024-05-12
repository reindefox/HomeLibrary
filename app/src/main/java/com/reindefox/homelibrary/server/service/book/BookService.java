package com.reindefox.homelibrary.server.service.book;

import com.reindefox.homelibrary.server.model.Book;

import java.util.Collection;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BookService {
    @POST("/book/all")
    Call<Collection<Book>> getAll(@Header("Authorization") String token);
}
