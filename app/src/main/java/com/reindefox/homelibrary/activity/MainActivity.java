package com.reindefox.homelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
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

        // Запуск анимации загрузки
        Glide.with(view)
                .load(R.drawable.fox_loading)
                .into(binding.foxLoading);

        webServer = WebServerSingleton.getInstance();

        createWebServerConnection();

        // TODO если в AccountManager есть запись, пропускаем авторизацию
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
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    initializeAuthorizationActivity();
                }
            }

            @Override
            public void onFailure(Call<TestConnectionData> call, Throwable throwable) {
                Glide.with(binding.getRoot().getRootView())
                        .load(R.drawable.baseline_error_outline_24)
                        .into(binding.foxLoading);

                binding.loadingText.setText(R.string.loading_db_fail);
            }
        });
    }

    /**
     * Инициализация активности с авторизацией
     */
    private void initializeAuthorizationActivity() {
        Intent intent = new Intent(this, AuthorizationActivity.class);

        startActivity(intent);

        // TODO Отправить токен на сервер, и если авторизован, сразу на главную страницу

        // Завершение активности загрузки
        finish();
    }
}