package com.reindefox.homelibrary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.activity.AbstractAuthActivity;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.model.Book;
import com.reindefox.homelibrary.server.service.book.BookCreateRequest;
import com.reindefox.homelibrary.server.service.book.BookService;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookEditFragment extends Fragment {

    public static final String IS_NEW_BOOK_CREATING = "new_book";

    private BookService bookService;

    private SharedPreferences sharedPreferences;

    private View view;

    private TextInputEditText title;

    private TextInputEditText author;

    private TextInputEditText image;

    private TextInputEditText description;

    public static BookEditFragment newInstance(String param1, String param2) {
        return new BookEditFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book_edit, container, false);

        sharedPreferences = getContext().getSharedPreferences(AuthorizationUtils.PREFS_USER, Context.MODE_PRIVATE);

        bookService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(BookService.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getBoolean(IS_NEW_BOOK_CREATING)) {
                initBookCreate();
            } else {
                initBookEdit();
            }
        }

        return view;
    }

    /**
     * Вызывается в случае создания книги
     */
    private void initBookCreate() {
        Button saveButton = view.findViewById(R.id.saveChangesButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyInputFields()) {
                    BookCreateRequest book = prepareBook();

                    bookService.create("Bearer " + sharedPreferences.getString(AbstractAuthActivity.ARG_AUTH_TOKEN_TYPE, ""), book)
                            .enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {

                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable throwable) {

                                }
                            });
                }
            }
        });
    }

    /**
     * Вызывается в случае изменения книги
     */
    private void initBookEdit() {
        Button deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.VISIBLE);
    }

    private boolean verifyInputFields() {
        title = view.findViewById(R.id.inputTitle);
        author = view.findViewById(R.id.inputAuthor);
        image = view.findViewById(R.id.inputImage);
        description = view.findViewById(R.id.inputDescription);

        AtomicBoolean flag = new AtomicBoolean(true);

        Arrays.asList(title, author, image).forEach(textInputEditText -> {
            if (textInputEditText.getText() != null && textInputEditText.getText().toString().isEmpty()) {
                flag.set(false);
            }
        });

        if (!flag.get()) {
            Snackbar.make(view, R.string.app_book_edit_fill_all, Snackbar.LENGTH_SHORT)
                    .show();
        }

        return flag.get();
    }

    private BookCreateRequest prepareBook() {
        BookCreateRequest book = new BookCreateRequest();

        book.setTitle(title.getText().toString());
        book.setAuthor(author.getText().toString());
        book.setImageUrl(image.getText().toString());
        book.setDescription(description.getText().toString());

        return book;
    }
}