package com.reindefox.homelibrary.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import com.reindefox.homelibrary.databinding.ActivityAuthorizationBinding;

import io.reactivex.rxjava3.core.Single;

/**
 * Класс активности авторизации пользователя
 */
public class AuthorizationActivity extends AppCompatActivity {

    /**
     * Биндинг элемента
     */
    private ActivityAuthorizationBinding binding;

    /**
     * Максимальная длина имени пользователя и пароля
     */
    public static final int MAX_USER_DATA_LENGTH = 32;

    RxDataStore<Preferences> dataStore;

    private static final Preferences.Key<String> LOGIN = PreferencesKeys.stringKey("login");

    private static final Preferences.Key<String> PASSWORD = PreferencesKeys.stringKey("password");

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
        binding = ActivityAuthorizationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dataStore = new RxPreferenceDataStoreBuilder(this, "user")
                .build();

        setupFieldLimitations();
        setupSignUpText();

        dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();

            mutablePreferences.set(LOGIN, "1");

            Log.i("1", "bruh");

            return Single.just(mutablePreferences);
        });

        binding.login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataStore.updateDataAsync(prefsIn -> {
                    MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();

                    mutablePreferences.set(LOGIN, s.toString());

                    return Single.just(mutablePreferences);
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Установка ограничений полей данных
     */
    private void setupFieldLimitations() {
        binding.login.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(MAX_USER_DATA_LENGTH)
        });

        binding.password.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(MAX_USER_DATA_LENGTH)
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String login = dataStore.data().firstOrError().map(prefs -> prefs.get(LOGIN)).onErrorReturnItem("null").blockingGet();
        // TODO хранилище данных
    }

    /**
     * Создание кнопки для перехода на активность регистрации
     */
    private void setupSignUpText() {
        SpannableString spannableString = new SpannableString(binding.signupText.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                redirectToSignUp();
            }
            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
            }
        };

        spannableString.setSpan(clickableSpan, 20, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.signupText.setText(spannableString);
        binding.signupText.setMovementMethod(LinkMovementMethod.getInstance());
        binding.signupText.setHighlightColor(Color.TRANSPARENT);
    }

    /**
     * Переход на активность регистрации
     */
    private void redirectToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);

        startActivity(intent);

        // TODO проверить
        finish();
    }
}