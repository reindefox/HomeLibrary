package com.reindefox.homelibrary.server.service.authorization;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthorizationService {
    @POST("/auth/login")
    @Headers("Content-Type: application/json")
    Call<AuthorizationDataResponse> login(@Body AuthorizationDataRequest request);

    @POST("/auth/reg")
    @Headers("Content-Type: application/json")
    Call<AuthorizationDataResponse> signUp(@Body AuthorizationDataRequest request);
}
