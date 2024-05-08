package com.reindefox.homelibrary.server.service.authorization;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizationService {
    @POST("/auth/login")
    Call<AuthorizationDataResponse> login(@Body AuthorizationDataRequest request);
}
