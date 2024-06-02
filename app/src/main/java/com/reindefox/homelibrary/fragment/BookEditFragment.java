package com.reindefox.homelibrary.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.activity.AbstractAuthActivity;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.model.Book;
import com.reindefox.homelibrary.server.service.book.BookDeleteRequest;
import com.reindefox.homelibrary.server.service.book.BookService;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookEditFragment extends Fragment {

    public static final String IS_NEW_BOOK_CREATING = "new_book";

    public static final String BOOK_DATA = "book_data";

    private BookService bookService;

    private SharedPreferences sharedPreferences;

    private View view;

    private TextInputEditText title;

    private TextInputEditText author;

    private TextInputEditText image;

    private TextInputEditText description;

    private TextInputEditText content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book_edit, container, false);

        sharedPreferences = getContext().getSharedPreferences(AuthorizationUtils.PREFS_USER, Context.MODE_PRIVATE);

        bookService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(BookService.class);

        title = view.findViewById(R.id.inputTitle);
        author = view.findViewById(R.id.inputAuthor);
        image = view.findViewById(R.id.inputImage);
        description = view.findViewById(R.id.inputDescription);
        content = view.findViewById(R.id.inputContent);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getBoolean(IS_NEW_BOOK_CREATING)) {
                initBookCreate();
            } else {
                initBookEdit();
            }
        }

        ImageView imageView = view.findViewById(R.id.bookPreviewImage);
        image.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Glide
                        .with(view)
                        .load(s.toString())
                        .error(R.drawable.imagesmode_24dp_fill0_wght400_grad0_opsz24)
                        .into(imageView);
            }
        });

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
                    createNewBook();
                }
            }
        });
    }

    /**
     * Вызывается в случае изменения книги
     */
    private void initBookEdit() {
        Book book = getArguments().getSerializable(BookEditFragment.BOOK_DATA, Book.class);

        if (book == null) {
            return;
        }

        Button deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(book);
            }
        });

        Button saveButton = view.findViewById(R.id.saveChangesButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyInputFields()) {
                    updateBook(book);
                }
            }
        });

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        image.setText(book.getImageUrl());
        description.setText(book.getDescription());
        content.setText(book.getContent());
    }

    private void updateBook(Book book) {
        bookService.update("Bearer " + sharedPreferences.getString(AbstractAuthActivity.ARG_AUTH_TOKEN_TYPE, ""), prepareBook(book.getId()))
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            createDialog(getString(R.string.app_book_edit_updated_info), book.getTitle())
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable throwable) {
                        Log.e(this.getClass().getSimpleName(), throwable.toString());
                    }
                });
    }

    private boolean verifyInputFields() {
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

    private void createNewBook() {
        Book book = prepareBook();

        bookService.create("Bearer " + sharedPreferences.getString(AbstractAuthActivity.ARG_AUTH_TOKEN_TYPE, ""), book)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.code() == HttpURLConnection.HTTP_CREATED) {
                            createDialog(getString(R.string.app_book_edit_created_info), book.getTitle())
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable throwable) {
                        Log.e(this.getClass().getSimpleName(), throwable.toString());
                    }
                });
    }

    private void deleteBook(final Book book) {
        BookDeleteRequest request = new BookDeleteRequest();

        request.setId(book.getId());

        bookService.delete("Bearer " + sharedPreferences.getString(AbstractAuthActivity.ARG_AUTH_TOKEN_TYPE, ""), request)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            createDialog(getString(R.string.app_book_edit_deleted_info), book.getTitle())
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable throwable) {
                        Log.e(this.getClass().getSimpleName(), throwable.toString());
                    }
                });
    }

    private Dialog createDialog(final String title, final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.appLayout, CatalogFragment.newInstance())
                                .commit();
                    }
                })
                .create();
    }

    private Book prepareBook() {
        Book book = new Book();

        book.setTitle(title.getText().toString());
        book.setAuthor(author.getText().toString());
        book.setImageUrl(image.getText().toString());
        book.setDescription(description.getText().toString());
        book.setContent(content.getText().toString());

        return book;
    }

    private Book prepareBook(final Integer withId) {
        Book prepared = prepareBook();

        prepared.setId(withId);

        return prepared;
    }
}