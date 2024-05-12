package com.reindefox.homelibrary.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Синглтон для установки подключения с удалённым сервером
 */
public class WebServerSingleton {
    private static WebServerSingleton webServer;

    private static Retrofit retrofit;

    public static final String WEB_SRV_URL = "http://10.0.2.2:8080/";

    public static WebServerSingleton getInstance() {
        if (webServer == null) {
            webServer = new WebServerSingleton();

            retrofit = new Retrofit.Builder()
                    .baseUrl(WEB_SRV_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return webServer;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
