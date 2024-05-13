package com.reindefox.homelibrary.fragment.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.fragment.BookFragment;
import com.reindefox.homelibrary.server.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.BookViewHolder> {

    private final FragmentActivity context;

    private List<Book> books;

    public BookRecyclerViewAdapter(FragmentActivity context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_card, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);

        holder.authorField.setText(book.getAuthor());
        holder.titleField.setText(book.getTitle());

        Glide
                .with(this.context)
                .load(book.getImageUrl())
                .into(holder.coverImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    BookFragment bookFragment = new BookFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BookFragment.bundleName, book);
                    bookFragment.setArguments(bundle);

                    context.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.appLayout, bookFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void searchFilter(ArrayList<Book> arrayList) {
        books = arrayList;
        notifyDataSetChanged();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImage;

        TextView authorField;

        TextView titleField;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            coverImage = itemView.findViewById(R.id.coverImage);
            authorField = itemView.findViewById(R.id.authorField);
            titleField = itemView.findViewById(R.id.titleField);
        }
    }
}
