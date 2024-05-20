package com.reindefox.homelibrary.fragment.component;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.reindefox.homelibrary.R;

public class LoadingDialog extends AlertDialog {

    private final Context context;

    private AlertDialog dialog;

    private boolean alreadyLoading = false;

    public LoadingDialog(Context context) {
        super(context);

        this.context = context;

        initialize();
    }

    public LoadingDialog(Context context, boolean startOnCreate) {
        this(context);

        if (startOnCreate)
            startLoading();
    }

    private void initialize() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        builder.setView(inflater.inflate(R.layout.loading, null));

        dialog = builder.create();

        // Убирает фон у анимации загрузки
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Запрещает принудительно останавливать анимацию
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void startLoading() {
        if (alreadyLoading)
            return;

        dialog.show();

        alreadyLoading = true;
    }

    public void stopLoading() {
        if (alreadyLoading)
            dialog.dismiss();

        alreadyLoading = false;
    }
}
