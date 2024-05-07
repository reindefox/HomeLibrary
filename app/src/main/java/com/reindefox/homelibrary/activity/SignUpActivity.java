package com.reindefox.homelibrary.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.databinding.ActivitySignUpBinding;

/**
 * Класс активности регистрации пользователя
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * Биндинг элемента
     */
    private ActivitySignUpBinding binding;

    /**
     * Разрешить регистрацию без принятия соглашения
     */
    private static boolean ENABLE_WITHOUT_EULA = false;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

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
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.signUpButton.setEnabled(ENABLE_WITHOUT_EULA);

        binding.checkBoxEULA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.signUpButton.setEnabled(isChecked);
            }
        });

        // TODO текст пароли не совпадают
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert binding.password.getText() != null;
                assert binding.repeatPassword.getText() != null;

                if (!binding.password.getText().toString().equals(binding.repeatPassword.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, R.string.login_signup_compare, Toast.LENGTH_SHORT)
                            .show();

                    return;
                }
            }
        });
    }
}