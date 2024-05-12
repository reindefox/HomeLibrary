package com.reindefox.homelibrary.server.service.authorization;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizationService {
    @POST("/auth/login")
    Call<SignInResponse> login(@Body AuthorizationDataRequest request);

    @POST("/auth/reg")
    Call<SignInResponse> signUp(@Body AuthorizationDataRequest request);
}
