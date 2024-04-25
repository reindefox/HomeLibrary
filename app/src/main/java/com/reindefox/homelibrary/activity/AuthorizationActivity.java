package com.reindefox.homelibrary.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.reindefox.homelibrary.databinding.ActivityAuthorizationBinding;

/**
 * Класс активности авторизации пользователя
 */
public class AuthorizationActivity extends AppCompatActivity {

    private ActivityAuthorizationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthorizationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupSignUpText();
    }

    private void setupSignUpText() {
        SpannableString spannableString = new SpannableString(binding.signupText.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {

            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        spannableString.setSpan(clickableSpan, 20, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.signupText.setText(spannableString);
        binding.signupText.setMovementMethod(LinkMovementMethod.getInstance());
        binding.signupText.setHighlightColor(Color.TRANSPARENT);
    }
}