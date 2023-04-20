package com.example.mylibrary.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.R;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.BitmapConverter;
import com.example.mylibrary.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.Base64;
import java.util.List;

public class BookReviewAdapter extends RecyclerView.Adapter<BookReviewAdapter.BookReviewMobile> {

    List<UserBook> userBooks;
    UserCallback userCallback;

    public BookReviewAdapter(List<UserBook> userBooks, UserCallback userCallback) {
        this.userBooks = userBooks;
        this.userCallback = userCallback;
    }

    public void addReview(List<UserBook> userBooks, int page) {
        int size = this.userBooks.size();
        if (size == page * 2) {
            this.userBooks.addAll(userBooks);
            this.notifyItemInserted(size);
        }
    }

    @NonNull
    @Override
    public BookReviewMobile onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_review, parent, false);

        return new BookReviewAdapter.BookReviewMobile(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull BookReviewMobile holder, int position) {
        if (userBooks.get(position).getUser().getImage() != "") {
            byte[] decode = Base64.getDecoder().decode(userBooks.get(position).getUser().getImage());
            Bitmap bitmap = BitmapConverter.fromBytes(decode);
            holder.userIcon.setImageBitmap(bitmap);
        }

        if(!userBooks.get(position).getUser().getName().isEmpty())
        holder.name.setText(userBooks.get(position).getUser().getName());
        if (!userBooks.get(position).getReview().isEmpty()) {
            holder.review.setVisibility(View.VISIBLE);
            holder.review.setText(userBooks.get(position).getReview());
        }

        holder.ratingBar.setRating(userBooks.get(position).getStars());
    }

    @Override
    public int getItemCount() {
        return userBooks.size();
    }

    public class BookReviewMobile extends RecyclerView.ViewHolder {

        ImageView userIcon;
        TextView name, review;
        RatingBar ratingBar;

        public BookReviewMobile(@NonNull View itemView) {
            super(itemView);

            userIcon = itemView.findViewById(R.id.userIcon);
            name = itemView.findViewById(R.id.name);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            review = itemView.findViewById(R.id.review);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userCallback.onUserReviewItemClick(getAdapterPosition());
                }
            });
        }
    }
}
