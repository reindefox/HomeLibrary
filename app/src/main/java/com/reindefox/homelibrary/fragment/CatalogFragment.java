package com.reindefox.homelibrary.fragment;

import static com.reindefox.homelibrary.activity.AuthActivityAbstract.ARG_AUTH_TOKEN_TYPE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.fragment.adapters.BookRecyclerViewAdapter;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.model.Book;
import com.reindefox.homelibrary.server.service.book.BookService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        books = new ArrayList<>();
        adapter = new BookRecyclerViewAdapter(this.getContext(), books);

        bookService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(BookService.class);

        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        sharedPreferences = getContext().getSharedPreferences(AuthorizationUtils.prefsUser, Context.MODE_PRIVATE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = view.getRootView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        TextInputEditText textInputEditText = view.findViewById(R.id.searchField);
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Book> arrayList = new ArrayList<>();

                for (Book book : books) {
                    if (book.getTitle().toLowerCase().contains(s.toString().toLowerCase())
                            || book.getAuthor().toLowerCase().contains(s.toString().toLowerCase())) {
                        arrayList.add(book);
                    }
                }

                adapter.searchFilter(arrayList);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        bookService.getAll("Bearer " + sharedPreferences.getString(ARG_AUTH_TOKEN_TYPE, ""))
                .enqueue(new Callback<Collection<Book>>() {
                    @Override
                    public void onResponse(Call<Collection<Book>> call, Response<Collection<Book>> response) {
                        books.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Collection<Book>> call, Throwable throwable) {
                    }
                });

        return view;
    }
}