package com.reindefox.homelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.databinding.ActivityMainBinding;

/**
 * Программный комлпекс - домашняя библиотека
 */
public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

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

        // TODO подключение к БД и потом редирект на страницу авторизации

        // TODO
        // initializeAuthorizationActivity();
    }

    private void createDatabaseConnection() {

    }

    private void initializeAuthorizationActivity() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
    }
}