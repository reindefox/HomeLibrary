package com.reindefox.homelibrary.activity;

import android.app.ActivityOptions;
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

    /**
     * Биндинг элемента
     */
    public ActivityMainBinding binding;

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

        // TODO Статус бар всегда видно должно быть
        // TODO подключение к БД и потом редирект на страницу авторизации
        if (!createDatabaseConnection()) {
            Glide.with(view)
                    .load(R.drawable.baseline_error_outline_24)
                    .into(binding.foxLoading);

            binding.loadingText.setText(R.string.loading_db_fail);

            return;
        }

        initializeAuthorizationActivity();
    }

    /**
     * Установка подключения к БД
     *
     * @return Состояние подключения
     */
    private boolean createDatabaseConnection() {
        return true;
    }

    /**
     * Инициализация активности с авторизацией
     */
    private void initializeAuthorizationActivity() {
        Intent intent = new Intent(this, AuthorizationActivity.class);

        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, findViewById(R.id.bookImage), "robot");

        startActivity(intent);

        // Завершение активности загрузки
        finish();
    }
}