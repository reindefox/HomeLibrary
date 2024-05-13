package com.reindefox.homelibrary.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reindefox.homelibrary.R;

public class ReadingFragment extends Fragment {

    public static ReadingFragment newInstance() {
        return new ReadingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }
}