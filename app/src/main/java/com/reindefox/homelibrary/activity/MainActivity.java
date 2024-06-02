package com.reindefox.homelibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.databinding.ActivityMainBinding;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.service.connection.TestConnectionData;
import com.reindefox.homelibrary.server.service.connection.TestConnectionService;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Программный комлпекс - домашняя библиотека
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Биндинг элемента
     */
    private ActivityMainBinding binding;

    /**
     * Синглтон для подключения к WS
     */
    private WebServerSingleton webServer;

    /**
     * Базовая инициализация компонента
     *
     * @param savedInstanceState Если действие повторно инициализируется после предыдущего закрытия,
     *                          то этот пакет содержит данные,
     *                          которые оно последним предоставило в {@link #onSaveInstanceState}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Log.i(this.getClass().getSimpleName(), "Application started");

        // Запуск анимации загрузки
        Glide.with(view)
                .load(R.drawable.fox_loading)
                .into(binding.foxLoading);

        webServer = WebServerSingleton.getInstance();
        createWebServerConnection();
    }

    /**
     * Установка подключения к веб-серверу
     */
    private void createWebServerConnection() {
        TestConnectionService testConnectionService = webServer.getRetrofit()
                .create(TestConnectionService.class);

        testConnectionService.test().enqueue(new Callback<TestConnectionData>() {
            @Override
            public void onResponse(Call<TestConnectionData> call, Response<TestConnectionData> response) {
                // Проверяем успешность подключения
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    // Переадресуем на авторизацию
                    initializeAuthorizationActivity(MainActivity.this);
                } else {
                    Log.e(this.getClass().getSimpleName(), "Failed to connect to server");
                    throwConnectionUIError();
                }
            }

            @Override
            public void onFailure(Call<TestConnectionData> call, Throwable throwable) {
                Log.e(this.getClass().getSimpleName(), throwable.toString());
                throwConnectionUIError();
            }
        });
    }

    /**
     * Инициализация активности с авторизацией
     */
    public static void initializeAuthorizationActivity(Context context) {
        Intent intent = new Intent(context, AuthorizationActivity.class);

        context.startActivity(intent);

        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    /**
     * Показывает текст, если не удалось установить подключение к серверу
     */
    private void throwConnectionUIError() {
        Glide.with(binding.getRoot().getRootView())
                .load(R.drawable.baseline_error_outline_24)
                .into(binding.foxLoading);

        binding.loadingText.setText(R.string.loading_db_fail);
    }
}