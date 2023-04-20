package com.example.mylibrary.fragments.bookDetailsActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.adapters.BookCallback;
import com.example.mylibrary.adapters.CoverAdapter;
import com.example.mylibrary.api.BooksApi;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Author;
import com.example.mylibrary.requestModel.Book;
import com.example.mylibrary.utils.ErrorManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorDetailsFragment extends Fragment implements BookCallback {
    public static final String TAG_FRAGMENT_AUTHOR_DETAILS = "TAG_FRAGMENT_AUTHOR_DETAILS";

    private OnFragmentActivityCommunication activityCommunication;

    private Author author;

    ImageView img;
    TextView name, description;
    RecyclerView rvBooks;
    List<Book> books;


    public static AuthorDetailsFragment newInstance() {
        Bundle args = new Bundle();

        AuthorDetailsFragment fragment = new AuthorDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_author_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        try {
            assert bundle != null;
            author = (Author) bundle.getSerializable("author");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }


        init(view);
        initBookList();

        setView();

    }

    private void setView() {
        Picasso.get().load(author.getPhoto()).into(img);
        name.setText(author.getName());
        description.setText(author.getDescription());
    }

    private void initBookList() {

        RetrofitService retrofitService = new RetrofitService();
        BooksApi booksApi = retrofitService.getRetrofit().create(BooksApi.class);


        booksApi.getAllByAuthorId(author.getId()).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    books = response.body();
                    setupBookAdapter();
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });

    }

    private void init(View view) {
        img = view.findViewById(R.id.img);
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);
        rvBooks = view.findViewById(R.id.rvBooksA);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getLayoutInflater().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        rvBooks.setLayoutManager(linearLayoutManager);
        rvBooks.setHasFixedSize(true);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentActivityCommunication) {
            activityCommunication = (OnFragmentActivityCommunication) context;
        }
    }

    private void setupBookAdapter() {
        CoverAdapter coverAdapter;
        coverAdapter = new CoverAdapter(books, this);
        rvBooks.setAdapter(coverAdapter);
    }


    @Override
    public void onBookItemClick(int pos) {
        Fragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", (Serializable) books.get(pos));
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
    }
}