package com.reindefox.homelibrary.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.reindefox.homelibrary.databinding.ActivityApplicationBinding;

/**
 * Основной интерфейс приложения
 */
public class ApplicationActivity extends AppCompatActivity {

    /**
     * Биндинг элемента
     */
    private ActivityApplicationBinding binding;

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
        binding = ActivityApplicationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}