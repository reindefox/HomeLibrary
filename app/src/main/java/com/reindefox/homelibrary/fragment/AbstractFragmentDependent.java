package com.reindefox.homelibrary.fragment;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class AbstractFragmentDependent extends AppCompatActivity {
    protected FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        fragmentManager = getSupportFragmentManager();
    }

    protected void replaceFragment(@IdRes int containerViewId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Полностью очищаем стек фрагментов
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }
}
