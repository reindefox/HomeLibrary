package com.reindefox.homelibrary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.activity.AbstractAuthActivity;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.auth.Role;
import com.reindefox.homelibrary.fragment.adapters.BookRecyclerViewAdapter;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.model.Book;
import com.reindefox.homelibrary.server.service.book.BookService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogFragment extends Fragment {

    private BookService bookService;

    private List<Book> books;

    private BookRecyclerViewAdapter adapter;

    private SharedPreferences sharedPreferences;

    public static CatalogFragment newInstance() {
        return new CatalogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        books = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        bookService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(BookService.class);

        adapter = new BookRecyclerViewAdapter(this.getActivity(), books);

        sharedPreferences = getContext().getSharedPreferences(AuthorizationUtils.PREFS_USER, Context.MODE_PRIVATE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        setupSearchFT(view);
        setupAdminManager(view);

        getAllBooks();

        return view;
    }

    private void setupAdminManager(View view) {
        if (sharedPreferences.getString(AbstractAuthActivity.ARG_USER_ROLE, "").equals(String.valueOf(Role.ADMIN))) {
            Button button = view.findViewById(R.id.addBookButton);

            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookEditFragment bookEditFragment = new BookEditFragment();

                    Bundle bundle = new Bundle();
                    bundle.putBoolean(BookEditFragment.IS_NEW_BOOK_CREATING, true);
                    bookEditFragment.setArguments(bundle);

                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.appLayout, bookEditFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    private void setupSearchFT(View view) {
        TextInputEditText textInputEditText = view.findViewById(R.id.searchField);
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applySearchFilter(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getAllBooks() {
        bookService.getAll("Bearer " + sharedPreferences.getString(AbstractAuthActivity.ARG_AUTH_TOKEN_TYPE, ""))
                .enqueue(new Callback<Collection<Book>>() {
                    @Override
                    public void onResponse(Call<Collection<Book>> call, Response<Collection<Book>> response) {
                        books.clear();

                        books.addAll(response.body());

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Collection<Book>> call, Throwable throwable) {
                    }
                });
    }

    private void applySearchFilter(String s) {
        ArrayList<Book> arrayList = new ArrayList<>();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(s.toLowerCase())
                    || book.getAuthor().toLowerCase().contains(s.toLowerCase())) {
                arrayList.add(book);
            }
        }

        adapter.searchFilter(arrayList);
    }
}