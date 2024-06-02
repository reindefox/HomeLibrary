package com.reindefox.homelibrary.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.activity.AbstractAuthActivity;
import com.reindefox.homelibrary.activity.MainActivity;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.auth.Role;

import java.util.Arrays;

public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    private WebView webView;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = getContext().getSharedPreferences(AuthorizationUtils.PREFS_USER, Context.MODE_PRIVATE);

        TextView textView = view.findViewById(R.id.signedUsername);
        textView.setText(String.format(getString(R.string.app_settings_user), sharedPreferences.getString(AbstractAuthActivity.ARG_USER_LOGIN, null)));

        if (sharedPreferences.getString(AbstractAuthActivity.ARG_USER_ROLE, "").equals(String.valueOf(Role.ADMIN))) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.admin_panel_settings_24dp_fill0_wght400_grad0_opsz24, 0, 0, 0);
            Arrays.stream(textView.getCompoundDrawables()).findFirst().get()
                    .setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(),
                            android.R.color.holo_orange_light), PorterDuff.Mode.SRC_IN));
        }

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

        Button helpButton = view.findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView webview = new WebView(getActivity());
                webview.getSettings().setJavaScriptEnabled(true);
                webview.loadUrl("file:///android_asset/help.html");

                RelativeLayout.LayoutParams paramsWebView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
                dialog.addContentView(webview, paramsWebView);
                dialog.show();
            }
        });

        return view;
    }

    private class HelpWebView extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }
}