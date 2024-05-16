package com.reindefox.homelibrary.server.service.book;

import com.reindefox.homelibrary.server.model.Book;

import java.net.HttpURLConnection;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookService {
    @GET("/book/all")
    Call<Collection<Book>> getAll(@Header("Authorization") String token);

    @GET("/book/user_all")
    Call<Collection<Book>> getAllByUser(@Header("Authorization") String token);

    @GET("/book/user_has/{id}")
    Call<Collection<Book>> checkUserReading(@Header("Authorization") String token, @Path("id") Integer id);

    @POST("/book/create")
    Call<Integer> create(@Header("Authorization") String token, @Body BookCreateRequest request);

    @POST("/book/delete")
    Call<Integer> delete(@Header("Authorization") String token, @Body BookDeleteRequest request);
}
