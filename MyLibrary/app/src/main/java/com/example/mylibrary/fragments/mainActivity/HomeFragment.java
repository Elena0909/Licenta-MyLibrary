package com.example.mylibrary.fragments.mainActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.adapters.CategoryAdapter;
import com.example.mylibrary.adapters.CategoryCallback;
import com.example.mylibrary.adapters.GenreAdapter;
import com.example.mylibrary.adapters.GenreCallback;
import com.example.mylibrary.api.BooksApi;
import com.example.mylibrary.api.CategoryApi;
import com.example.mylibrary.api.GenreApi;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Book;
import com.example.mylibrary.requestModel.Category;
import com.example.mylibrary.requestModel.Genre;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements GenreCallback, CategoryCallback {
    public static final String TAG_FRAGMENT_HOME = "TAG_FRAGMENT_HOME";

    private OnFragmentActivityCommunication activityCommunication;
    private RecyclerView genresRV;
    private RecyclerView categoriesRV;
    private List<Category> categories;
    private List<Genre> genres;
    private ImageView searchIcon;
    private EditText searchBar;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        callGenres();
        callCaterory();

        searchBar.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (!searchBar.getText().toString().isEmpty())
                    callSearch(searchBar.getText().toString());
                return true;
            }
            return false;
        });

        searchIcon.setOnClickListener(v -> {
            if (!searchBar.getText().toString().isEmpty())
                callSearch(searchBar.getText().toString());
        });


    }

    private void callSearch(String text) {

        if (text.isEmpty()) {
            Toast.makeText(getActivity(), "Enter something in the search bar", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitService retrofitService = new RetrofitService();
        BooksApi booksApi = retrofitService.getRetrofit().create(BooksApi.class);

        booksApi.search(text, Utils.FIRST_PAGE).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> books = response.body();
                    goToBooks(books, Utils.SEARCH, text);
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

    private void callGenres() {

        RetrofitService retrofitService = new RetrofitService();
        GenreApi genreApi = retrofitService.getRetrofit().create(GenreApi.class);

        genreApi.allGenres().enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.isSuccessful()) {
                    genres = response.body();
                    setupGenreAdapter();
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });


    }

    private void setupGenreAdapter() {

        GenreAdapter bookAdapter = new GenreAdapter(genres, this);
        genresRV.setAdapter(bookAdapter);

    }

    private void setupCategoreAdapter() {

        CategoryAdapter bookAdapter = new CategoryAdapter(categories, this);
        categoriesRV.setAdapter(bookAdapter);

    }

    private void init(View view) {

        genresRV = view.findViewById(R.id.rvGenres);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        genresRV.setLayoutManager(linearLayoutManager);

        categoriesRV = view.findViewById(R.id.rvCategories);
        categoriesRV.setLayoutManager(new GridLayoutManager(getContext(), 3));

        searchBar = view.findViewById(R.id.search_bar);
        searchIcon = view.findViewById(R.id.search_icon);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentActivityCommunication) {
            activityCommunication = (OnFragmentActivityCommunication) context;
        }
    }

    private void callCaterory() {
        RetrofitService retrofitService = new RetrofitService();
        CategoryApi categoryApi = retrofitService.getRetrofit().create(CategoryApi.class);

        categoryApi.allCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categories = response.body();
                    setupCategoreAdapter();
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });
    }

    @Override
    public void onGenreItemClick(int pos) {

        RetrofitService retrofitService = new RetrofitService();
        BooksApi booksApi = retrofitService.getRetrofit().create(BooksApi.class);

        Genre genre = genres.get(pos);

        booksApi.getAllByGenre(genre.getName(), Utils.FIRST_PAGE).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> books = response.body();
                    goToBooks(books, Utils.GENRE, genre.getName());
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

    @Override
    public void onCategoryItemClick(int pos) {
        RetrofitService retrofitService = new RetrofitService();
        BooksApi booksApi = retrofitService.getRetrofit().create(BooksApi.class);

        Category category = categories.get(pos);

        booksApi.getAllByCategory(category.getName(), Utils.FIRST_PAGE).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> books = response.body();
                    goToBooks(books, Utils.CATEGORY, category.getName());
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

    public void goToBooks(List<Book> books, int option, String text) {
        try {
            if (books == null)
                books = new ArrayList<>();
            Fragment fragment = new BooksFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("books", (Serializable) books);
            bundle.putInt("option", option);
            bundle.putString("text", text);
            fragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        } catch (RuntimeException exception) {
            if (getContext() != null && getActivity() != null) {
                Toast.makeText(getActivity(), "Error fragment :((" + exception.getMessage(), Toast.LENGTH_LONG).show();
                goToBooks(books, option, text);
            }
            System.out.println("error: " + exception.getMessage());
        }
    }
}