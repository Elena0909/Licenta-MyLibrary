package com.example.mylibrary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.R;
import com.example.mylibrary.requestModel.UserBook;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserBookAdapter extends RecyclerView.Adapter<UserBookAdapter.UserBookMobile> {


    List<UserBook> userBooks;
    BookCallback callback;

    public UserBookAdapter(List<UserBook> userBooks, BookCallback bookCallback) {
        this.userBooks = userBooks;
        this.callback = bookCallback;
    }

    public UserBookAdapter(List<UserBook> userBooks) {
        this.userBooks = userBooks;
        callback = null;
    }

    public void addBooks(List<UserBook> userBooks, int page) {
        int size = this.userBooks.size();
        if (size == page * 15) {
            this.userBooks.addAll(userBooks);
            this.notifyItemInserted(size);
        }
    }

    @NonNull
    @Override
    public UserBookAdapter.UserBookMobile onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (callback != null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_book, parent, false);
            return new UserBookAdapter.UserBookMobile(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_cover, parent, false);
        return new UserBookAdapter.UserBookMobile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserBookAdapter.UserBookMobile holder, int position) {
        UserBook userBook = userBooks.get(position);

        Picasso.get().load(userBook.getBook().getPhoto()).into(holder.imgBook);

        if (callback != null && userBook.getStars() != null) {
            holder.ratingBar.setRating(userBook.getStars());
            holder.ratingBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return userBooks.size();
    }

    public class UserBookMobile extends RecyclerView.ViewHolder {

        ImageView imgBook;
        RatingBar ratingBar;

        public UserBookMobile(@NonNull View itemView) {
            super(itemView);

            imgBook = itemView.findViewById(R.id.img);
            if (callback != null)
                ratingBar = itemView.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.onBookItemClick(getAdapterPosition());
                }
            });
        }
    }
}
