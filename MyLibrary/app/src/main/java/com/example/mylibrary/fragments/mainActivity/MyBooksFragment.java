package com.example.mylibrary.fragments.mainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.activities.BookDetailsActivity;
import com.example.mylibrary.adapters.BookCallback;
import com.example.mylibrary.adapters.UserBookAdapter;
import com.example.mylibrary.api.UserBookApi;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.Status;
import com.example.mylibrary.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyBooksFragment extends Fragment implements BookCallback {

    public static final String TAG_FRAGMENT_MY_BOOKS = "TAG_FRAGMENT_MY_BOOKS";

    private OnFragmentActivityCommunication activityCommunication;


    private RecyclerView booksRV;
    private UserBookAdapter bookAdapter;
    private Button want, current, read;
    private List<UserBook> userBooks = new ArrayList<>();
    private int page = Utils.FIRST_PAGE;
    private Long status;
    private boolean lastPage;

    public static MyBooksFragment newInstance() {
        Bundle args = new Bundle();

        MyBooksFragment fragment = new MyBooksFragment();
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
        return inflater.inflate(R.layout.fragment_my_books, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        status = 1L;

        Bundle bundle = this.getArguments();
        try {
            assert bundle != null;
            status = (Long) bundle.getSerializable("status");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        init(view);

        initList(status, view);

        want.setOnClickListener(v -> {
            if (status != Status.WANT) {
                lastPage = false;
                page = Utils.FIRST_PAGE;
            }
            initList(Status.WANT, v);
        });

        current.setOnClickListener(v -> {
            if (status != Status.CURRENT) {
                lastPage = false;
                page = Utils.FIRST_PAGE;
            }
            initList(Status.CURRENT, v);
        });

        read.setOnClickListener(v -> {
            if (status != Status.READ) {
                lastPage = false;
                page = Utils.FIRST_PAGE;
            }
            initList(Status.READ, v);
        });

        booksRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    if (lastPage) {
                        Snackbar.make(view, "Last books", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    Snackbar.make(view, "Loading...", Snackbar.LENGTH_LONG).show();

                    initList(status, view);

                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    Snackbar.make(view, "First", Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setColor(Button select, Button other1, Button other2) {
        if (getContext() != null) {
            select.setBackgroundColor(getContext().getResources().getColor(R.color.select_yellow));
            other1.setBackgroundColor(getContext().getResources().getColor(R.color.yellow));
            other2.setBackgroundColor(getContext().getResources().getColor(R.color.yellow));
        }
    }

    private void initList(Long idStatus, View view) {


        RetrofitService retrofitService = new RetrofitService();
        UserBookApi userBookApi = retrofitService.getRetrofit().create(UserBookApi.class);

        userBookApi.getFilteredBooks(idStatus, page).enqueue(new Callback<List<UserBook>>() {
            @Override
            public void onResponse(Call<List<UserBook>> call, Response<List<UserBook>> response) {
                if (!response.isSuccessful()) {
                    ErrorManager.getErrorMessage(getContext(), response);
                    return;
                }

                if (response.body() == null) {
                    if (getContext() != null)
                        Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                    return;
                }

                if (!status.equals(idStatus) || page == Utils.FIRST_PAGE) {

                    userBooks.clear();
                    userBooks = response.body();
                    status = idStatus;
                    page++;
                    if (userBooks.size() == 0) {
                        Snackbar.make(view, "Your list is empty", Snackbar.LENGTH_LONG).show();
                    }

                    if (idStatus.equals(Status.WANT))
                        setColor(want, current, read);
                    if (idStatus.equals(Status.CURRENT))
                        setColor(current, want, read);
                    if (idStatus.equals(Status.READ))
                        setColor(read, current, want);

                    setupBookAdapter();
                    return;
                }
                if (response.body().isEmpty()) {
                    lastPage = true;
                    Snackbar.make(view, "Last books", Snackbar.LENGTH_LONG).show();
                    return;
                }
                bookAdapter.addBooks(response.body(), page);
                page++;

            }

            @Override
            public void onFailure(Call<List<UserBook>> call, Throwable t) {
                ErrorManager.failCall(getContext(),t);
            }
        });
    }

    private void init(View view) {
        want = view.findViewById(R.id.want);
        current = view.findViewById(R.id.current);
        read = view.findViewById(R.id.read);
        booksRV = view.findViewById(R.id.rvBooks);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        booksRV.setLayoutManager(gridLayoutManager);
    }

    private void setupBookAdapter() {
        bookAdapter = new UserBookAdapter(userBooks, this);
        booksRV.setAdapter(bookAdapter);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentActivityCommunication) {
            activityCommunication = (OnFragmentActivityCommunication) context;
        }
    }

    @Override
    public void onBookItemClick(int pos) {
        Intent i = new Intent(this.requireContext(), BookDetailsActivity.class);
        i.putExtra("book", userBooks.get(pos).getBook());
        startActivity(i);
        requireActivity().finish();
    }
}