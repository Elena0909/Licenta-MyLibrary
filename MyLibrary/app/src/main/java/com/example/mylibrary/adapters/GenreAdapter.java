package com.example.mylibrary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.R;
import com.example.mylibrary.requestModel.Genre;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreMobile> {

    List<Genre> genres;
    GenreCallback g;


    public GenreAdapter(List<Genre> genres, GenreCallback callback) {
        this.genres=genres;
        this.g=callback;
    }

    @NonNull
    @Override
    public GenreMobile onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);

        return new GenreMobile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreMobile holder, int position) {
        holder.name.setText(genres.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }


    public class GenreMobile extends RecyclerView.ViewHolder {

        TextView name;

        public GenreMobile(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_genre);

          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  g.onGenreItemClick(getAdapterPosition());
              }
          });
        }
    }
}
