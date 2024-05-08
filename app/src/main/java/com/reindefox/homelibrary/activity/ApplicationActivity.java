package com.reindefox.homelibrary.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationBarView;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.databinding.ActivityApplicationBinding;
import com.reindefox.homelibrary.fragment.CatalogFragment;
import com.reindefox.homelibrary.fragment.FragmentDependent;

/**
 * Основной интерфейс приложения
 */
public class ApplicationActivity extends FragmentDependent {

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

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.catalog) {
                    replaceFragment(R.id.appLayout, CatalogFragment.newInstance());
                } else if (menuItem.getItemId() == R.id.reading) {
                    // TODO
                    replaceFragment(R.id.appLayout, null);
                } else if (menuItem.getItemId() == R.id.settings) {
                    // TODO
                    replaceFragment(R.id.appLayout, null);
                }

                return false;
            }
        });
    }
}