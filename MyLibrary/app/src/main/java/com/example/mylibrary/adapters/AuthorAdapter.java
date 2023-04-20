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
import com.example.mylibrary.requestModel.Author;
import com.example.mylibrary.requestModel.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorMobile>{

    List<Author> authors;
    AuthorCallback callback;


    public AuthorAdapter(List<Author> authors, AuthorCallback callback) {
      this.authors = authors;
      this.callback = callback;
    }


    @NonNull
    @Override
    public AuthorAdapter.AuthorMobile onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author, parent, false);

        return new AuthorAdapter.AuthorMobile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.AuthorMobile holder, int position) {
        holder.author.setText(authors.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return authors.size();
    }


    public class AuthorMobile extends RecyclerView.ViewHolder {

        TextView author;
        public AuthorMobile(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onAuthorItemClick(getAdapterPosition());
                }
            });
        }
    }
}


