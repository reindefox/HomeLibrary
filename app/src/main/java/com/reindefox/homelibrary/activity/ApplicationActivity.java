package com.reindefox.homelibrary.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationBarView;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.databinding.ActivityApplicationBinding;
import com.reindefox.homelibrary.fragment.AbstractFragmentDependent;
import com.reindefox.homelibrary.fragment.CatalogFragment;
import com.reindefox.homelibrary.fragment.ReadingFragment;
import com.reindefox.homelibrary.fragment.SettingsFragment;

/**
 * Основная активность приложения
 */
public class ApplicationActivity extends AbstractFragmentDependent {

    /**
     * Биндинг элемента
     */
    private ActivityApplicationBinding binding;

    /**
     * Базовая инициализация компонента
     *
     * @param savedInstanceState Если действие повторно инициализируется после предыдущего закрытия,
     *                           то этот пакет содержит данные,
     *                           которые оно последним предоставило в {@link #onSaveInstanceState}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplicationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initializeBasicFragment();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.catalog) {
                    replaceFragment(R.id.appLayout, CatalogFragment.newInstance());
                } else if (menuItem.getItemId() == R.id.reading) {
                    replaceFragment(R.id.appLayout, ReadingFragment.newInstance());
                } else if (menuItem.getItemId() == R.id.settings) {
                    replaceFragment(R.id.appLayout, SettingsFragment.newInstance());
                }

                return true;
            }
        });
    }

    public ActivityApplicationBinding getBinding() {
        return binding;
    }

    private void initializeBasicFragment() {
        replaceFragment(R.id.appLayout, CatalogFragment.newInstance());
    }
}