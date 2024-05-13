package com.reindefox.homelibrary.fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.server.model.Book;

public class BookFragment extends Fragment {

    public static final String bundleName = "book";

    private View view;

    public static BookFragment newInstance() {
        return new BookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book, container, false);

        TextView descView = view.findViewById(R.id.descriptionText);
        descView.setMovementMethod(new ScrollingMovementMethod());

        setBookData();

        return view;
    }

    private void getUserBookData() {

    }

    private void setBookData() {
        Bundle bundle = getArguments();

        assert bundle != null;
        Book book = bundle.getSerializable(bundleName, Book.class);
        
        if (book == null) {
            Snackbar.make(view, R.string.error, Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }

        ImageView imageView = view.findViewById(R.id.coverImageBook);
        Glide
                .with(view)
                .load(book.getImageUrl())
                .into(imageView);

        TextView titleView = view.findViewById(R.id.titleBook);
        titleView.setText(book.getTitle());

        TextView authorView = view.findViewById(R.id.authorBook);
        authorView.setText(book.getAuthor());

        TextView descView = view.findViewById(R.id.descriptionText);
        descView.setText(book.getDescription());
    }
}