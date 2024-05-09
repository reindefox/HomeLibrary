package com.reindefox.homelibrary.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.fragment.adapters.BookRecyclerViewAdapter;
import com.reindefox.homelibrary.server.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment {

    private List<TableRow> rows;

    private List<Book> books;

    private BookRecyclerViewAdapter adapter;

    public CatalogFragment() {
    }

    public static CatalogFragment newInstance() {
        return new CatalogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        books = new ArrayList<>();
        Book book = new Book();
        book.setAuthor("asd");
        book.setTitle("sdd");
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);
        books.add(book);

        adapter = new BookRecyclerViewAdapter(this.getContext(), books);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = view.getRootView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void addBook() {

    }

    private TableRow insertNewRowForBooks() {
        TableRow tableRow = new TableRow(this.getContext());

        return tableRow;
    }
}