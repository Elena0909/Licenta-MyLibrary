package com.example.mylibrary.adapters;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.R;
import com.example.mylibrary.api.BooksApi;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Book;
import com.example.mylibrary.requestModel.UserBook;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookMobile> {

    List<Book> books;
    BookCallback callback;


    public BookAdapter(List<Book> books, BookCallback bookCallback) {
        this.books = books;
        this.callback = bookCallback;
    }

    public BookAdapter(List<Book> books) {
        this.books = books;
    }

    public void addBooks(List<Book> books, int page) {
        int size = this.books.size();
        if (size == page * 15) {
            this.books.addAll(books);
            this.notifyItemInserted(size);
        }
    }


    @NonNull
    @Override
    public BookMobile onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookMobile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookMobile holder, int position) {
        Picasso.get().load(books.get(position).getPhoto()).into(holder.imgBook);
        holder.title.setText(books.get(position).getName());
        holder.ratingBar.setRating(books.get(position).getStars());
        holder.pages.setText(books.get(position).getPages() + " ");
        holder.author.setText(books.get(position).getAuthors().get(0).getName());
        if (books.get(position).getAuthors().size() != 1)
            holder.author.setText(holder.author.getText() + "...");
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookMobile extends RecyclerView.ViewHolder {

        ImageView imgBook, imgContainer;
        TextView title, author, pages;
        RatingBar ratingBar;

        public BookMobile(@NonNull View itemView) {
            super(itemView);

            imgBook = itemView.findViewById(R.id.img);
            imgContainer = itemView.findViewById(R.id.bg);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            pages = itemView.findViewById(R.id.pages);
            ratingBar = itemView.findViewById(R.id.ratingBar);



            itemView.setOnClickListener(
                    v -> callback.onBookItemClick(getAdapterPosition()));
        }
    }
}


