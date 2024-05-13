package com.reindefox.homelibrary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.activity.MainActivity;
import com.reindefox.homelibrary.auth.AuthorizationUtils;

public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = getContext().getSharedPreferences(AuthorizationUtils.PREFS_USER, Context.MODE_PRIVATE);

        Switch switchCompat = view.findViewById(R.id.keepLoginSwitch);
        switchCompat.setChecked(sharedPreferences.getBoolean(AuthorizationUtils.PREFS_AUTO_LOGIN, false));
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean(AuthorizationUtils.PREFS_AUTO_LOGIN, isChecked).apply();
            }
        });

        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().clear().apply();

                MainActivity.initializeAuthorizationActivity(view.getContext());
            }
        });

        return view;
    }
}