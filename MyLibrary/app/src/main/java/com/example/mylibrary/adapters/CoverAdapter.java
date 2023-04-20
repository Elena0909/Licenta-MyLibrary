package com.example.mylibrary.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.R;
import com.example.mylibrary.requestModel.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.CoverMobile> {

    List<Book> books;
    BookCallback callback;


    public CoverAdapter(List<Book> books, BookCallback bookCallback) {
        this.books = books;
        this.callback = bookCallback;
    }

    public CoverAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public CoverMobile onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cover, parent, false);

        return new CoverAdapter.CoverMobile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoverMobile holder, int position) {
        Picasso.get().load(books.get(position).getPhoto()).into(holder.imgBook);

        if(books.get(position).getVolume()!=null) {
            holder.volume.setText(holder.volume.getText() + " " + books.get(position).getVolume());
            holder.volume.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class CoverMobile extends RecyclerView.ViewHolder {

        ImageView imgBook;
        TextView volume;

        public CoverMobile(@NonNull View itemView) {
            super(itemView);

            imgBook = itemView.findViewById(R.id.img);
            volume = itemView.findViewById(R.id.vol);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onBookItemClick(getAdapterPosition());
                }
            });
        }
    }
}
