package com.reindefox.homelibrary.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

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

        binding.repeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = binding.password.getText().toString();
                String repeatPassword = binding.repeatPassword.getText().toString();

                if (!password.equals(repeatPassword)) {
//                    binding.repeatPassword
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // TODO текст пароли не совпадают
    }
}