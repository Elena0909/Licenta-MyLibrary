package com.example.mylibrary.fragments.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.activities.BookDetailsActivity;
import com.example.mylibrary.adapters.BookAdapter;
import com.example.mylibrary.adapters.BookCallback;
import com.example.mylibrary.api.BooksApi;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Book;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksFragment extends Fragment implements BookCallback {

    public static final String TAG_FRAGMENT_BOOKS = "TAG_FRAGMENT_BOOKS";
    private List<Book> books;
    private RecyclerView booksRV;
    private ImageView searchIcon;
    private EditText searchBar;
    private int page = 1;
    private BookAdapter bookAdapter;
    private int option;
    private String text;
    private boolean maxPage = false;
    private boolean loading = false;

    private OnFragmentActivityCommunication activityCommunication;

    public static BooksFragment newInstance() {
        Bundle args = new Bundle();

        BooksFragment fragment = new BooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentActivityCommunication) {
            activityCommunication = (OnFragmentActivityCommunication) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        try {
            assert bundle != null;
            books = (List<Book>) bundle.getSerializable("books");
            option = bundle.getInt("option");
            text = bundle.getString("text");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        init(view);

        if (books.isEmpty() && getContext() != null) {
            Toast.makeText(getContext(), Html.fromHtml("<font color='#FF9800' ><b>" + getString(R.string.book_not_find) + text + "</b></font>"), Toast.LENGTH_LONG).show();
            setupBookAdapter();
        }


        searchBar.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (!searchBar.getText().toString().isEmpty()) {


                    option = Utils.SEARCH;
                    page = Utils.FIRST_PAGE;
                    maxPage = false;
                    text = searchBar.getText().toString();

                    callSearch(searchBar.getText().toString(), false);
                }
                return true;
            }
            return false;
        });

        searchIcon.setOnClickListener(v -> {
            if (!searchBar.getText().toString().isEmpty()) {

                option = Utils.SEARCH;
                page = Utils.FIRST_PAGE;
                maxPage = false;
                text = searchBar.getText().toString();

                callSearch(searchBar.getText().toString(), false);
            }
        });


        booksRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    if (maxPage) {
                        Toast.makeText(getContext(), "Last book", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    switch (option) {
                        case Utils.SEARCH:
                            loading = true;
                            callSearch(text, true);
                            break;
                        case Utils.CATEGORY:
                            loading = true;
                            booksCategory(text);
                            break;
                        case Utils.GENRE:
                            loading = true;
                            booksGenre(text);
                            break;
                    }

                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    Toast.makeText(getContext(), "First", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void booksGenre(String text) {


        RetrofitService retrofitService = new RetrofitService();
        BooksApi booksApi = retrofitService.getRetrofit().create(BooksApi.class);


        booksApi.getAllByGenre(text, page).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    loading = false;

                    if (response.body() == null) {
                        if (getContext() != null)
                            Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (response.body().isEmpty()) {
                        Toast.makeText(getContext(), "Last book", Toast.LENGTH_LONG).show();
                        maxPage = true;
                        return;
                    }

                    bookAdapter.addBooks(response.body(), page);
                    page++;
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


    public void booksCategory(String text) {

        RetrofitService retrofitService = new RetrofitService();
        BooksApi booksApi = retrofitService.getRetrofit().create(BooksApi.class);


        booksApi.getAllByCategory(text, page).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    loading = false;

                    if (response.body() == null) {
                        if (getContext() != null)
                            Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                        return;
                    }


                    if (response.body().isEmpty()) {
                        Toast.makeText(getContext(), "Last book", Toast.LENGTH_LONG).show();
                        maxPage = true;
                        return;
                    }

                    bookAdapter.addBooks(response.body(), page);
                    page++;
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
        booksRV = view.findViewById(R.id.rvBooks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        booksRV.setLayoutManager(linearLayoutManager);

        setupBookAdapter();

        searchBar = view.findViewById(R.id.search_bar);
        searchIcon = view.findViewById(R.id.search_icon);
    }


    private void callSearch(String search, boolean scroll) {

        if (search.isEmpty()) {
            return;
        }


        RetrofitService retrofitService = new RetrofitService();
        BooksApi booksApi = retrofitService.getRetrofit().create(BooksApi.class);


        booksApi.search(search, page).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    loading = false;

                    if (response.body() == null) {
                        if (getContext() != null)
                            Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (page == Utils.FIRST_PAGE) {
                        if (response.body().isEmpty() && getContext()!=null) {
                            Toast.makeText(getContext(), Html.fromHtml("<font color='#FF9800' ><b>" + getString(R.string.book_not_find) + search + "</b></font>"), Toast.LENGTH_LONG).show();
                            setupBookAdapter();
                        }
                        page++;
                        if (scroll) {
                            bookAdapter.addBooks(response.body(), page);
                            return;
                        }
                        books.clear();
                        books = response.body();
                        setupBookAdapter();
                        return;

                    }

                    if (response.body().isEmpty()) {
                        maxPage = true;
                        Toast.makeText(getContext(), "Last book", Toast.LENGTH_LONG).show();
                        return;
                    }

                    bookAdapter.addBooks(response.body(), page);
                    page++;
                    loading = false;
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

    private void setupBookAdapter() {
        bookAdapter = new BookAdapter(books, this);
        booksRV.setAdapter(bookAdapter);
    }


    @Override
    public void onBookItemClick(int pos) {
        Intent i = new Intent(this.requireContext(), BookDetailsActivity.class);
        i.putExtra("book", books.get(pos));
        startActivity(i);
        requireActivity().finish();
    }
}