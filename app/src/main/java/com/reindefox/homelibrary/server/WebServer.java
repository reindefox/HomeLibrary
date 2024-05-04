package com.reindefox.homelibrary.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Синглтон для установки подключения с удалённым сервером
 */
public class WebServer {
    private static WebServer webServer;

    private static Retrofit retrofit;

    public static final String WEB_SRV_URL = "http://10.0.2.2:8080/";

    public static WebServer getInstance() {
        if (webServer == null) {
            webServer = new WebServer();

            retrofit = new Retrofit.Builder()
                    .baseUrl(WEB_SRV_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return webServer;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
