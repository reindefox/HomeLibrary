package com.reindefox.homelibrary.server.service.connection;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestConnectionService {
    @GET("/test")
    Call<TestConnectionData> test();
}
