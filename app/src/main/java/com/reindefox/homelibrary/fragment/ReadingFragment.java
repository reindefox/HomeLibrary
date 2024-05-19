package com.reindefox.homelibrary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.model.Book;
import com.reindefox.homelibrary.server.service.book.BookService;

import io.noties.markwon.Markwon;

public class ReadingFragment extends Fragment {

    private BookService bookService;

    private SharedPreferences sharedPreferences;

    private View view;

    public static ReadingFragment newInstance() {
        return new ReadingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reading, container, false);

        bookService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(BookService.class);

        sharedPreferences = getContext().getSharedPreferences(AuthorizationUtils.PREFS_USER, Context.MODE_PRIVATE);

        TextView textView = view.findViewById(R.id.contentField);

        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable(BookFragment.bundleName, Book.class) == null) {
            createMarkdown("**Выберите книгу в каталоге для предпросмотра**");
            ImageView imageView = view.findViewById(R.id.bookPointerImage);
            imageView.setVisibility(View.VISIBLE);

            return view;
        }

        Book book = bundle.getSerializable(BookFragment.bundleName, Book.class);
        createMarkdown(book.getContent());

        return view;
    }

    /**
     * Создать разметку для предпросмотра содержимого книги
     * @param text содержимое
     */
    private void createMarkdown(final String text) {
        final Markwon markwon = Markwon.create(getContext());

        TextView textView = view.findViewById(R.id.contentField);

        markwon.setMarkdown(textView, text);
    }
}