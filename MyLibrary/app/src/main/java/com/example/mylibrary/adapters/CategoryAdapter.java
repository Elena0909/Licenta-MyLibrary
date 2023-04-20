package com.example.mylibrary.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.R;
import com.example.mylibrary.requestModel.Category;
import com.example.mylibrary.requestModel.Genre;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryMobile> {

    List<Category> categories;
    CategoryCallback callback;


    public CategoryAdapter(List<Category> categories, CategoryCallback categoryCallback) {
        this.categories=categories;
        this.callback = categoryCallback;
    }

    @NonNull
    @Override
    public CategoryMobile onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new CategoryMobile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryMobile holder, int position) {
        String name = categories.get(position).getName();
        holder.name.setText(name);
        String photoURL = categories.get(position).getPhotoURL();
//        switch (name){
//            case "Fiction":
//                photoURL = "https://i.pinimg.com/originals/ef/cf/08/efcf08aff39a715db38a1637f5813a4d.jpg";
//                break;
//            case "Non-fiction":
//                photoURL = "https://i.pinimg.com/originals/8c/7f/ee/8c7feefdb1e5dc52d9bfb7694578407e.jpg";
//                break;
//            case "Short stories":
//                photoURL ="https://i.pinimg.com/736x/e3/29/d0/e329d024ec468aa97b56875c4728255c.jpg";
//                break;
//            case "Poetry":
//                photoURL = "https://i.pinimg.com/originals/e5/bf/00/e5bf009d3b83f68ac2b21e48728376bc.jpg";
//                break;
//            case "Theatre":
//                photoURL ="https://i.pinimg.com/originals/6d/76/c2/6d76c2018cf35c1ec3a3dff4b3bea01a.jpg";
//                break;
//            case "Graphic Novel":
//                photoURL ="https://i.pinimg.com/originals/fc/d2/4b/fcd24b05892c95f476f080d6cdd7239d.jpg";
//                break;
//            default:
//                photoURL = "https://cdn2.iconfinder.com/data/icons/documents-and-files-v-2/100/doc-03-512.png";
//        }
        Picasso.get().load(photoURL).into(holder.backgroud);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class CategoryMobile extends RecyclerView.ViewHolder {

        TextView name;
        ImageView backgroud;
        public CategoryMobile(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_category);
            backgroud = itemView.findViewById(R.id.background);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onCategoryItemClick(getAdapterPosition());
                }
            });
        }
    }
}
