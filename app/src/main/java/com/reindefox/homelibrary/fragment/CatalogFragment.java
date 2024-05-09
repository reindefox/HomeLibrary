package com.reindefox.homelibrary.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.views.Book;

import java.util.List;

/**
 * TODO
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment {

    private List<TableRow> rows;

    public CatalogFragment() {
    }

    public static CatalogFragment newInstance() {
        return new CatalogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        TableLayout tableLayout = view.findViewById(R.id.books);

        TableRow tableRow = new TableRow(this.getContext());
        tableRow.setLayoutParams(
                new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );

//
//        Book book = new Book(this.getContext());
//        book.setLayoutParams(
//                new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1)
//        );
//        tableRow.addView(book);
//
//        tl.addView(tableRow, new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return view;
    }

    private void addBook() {

    }

    private TableRow insertNewRowForBooks() {
        TableRow tableRow = new TableRow(this.getContext());

        return tableRow;
    }
}