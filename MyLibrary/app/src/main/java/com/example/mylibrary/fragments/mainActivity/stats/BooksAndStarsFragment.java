package com.example.mylibrary.fragments.mainActivity.stats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.adapters.UserBookAdapter;
import com.example.mylibrary.api.StatsApi;
import com.example.mylibrary.api.UserBookApi;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.StatsUtils;
import com.example.mylibrary.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksAndStarsFragment extends Fragment {


    private RecyclerView books5Stars, books4Stars, books3Stars, books2Stars, books1Stars;
    private StatsUtils statsUtils;
    private EditText yearET;
    TextView no5, no4, no3, no2, no1;
    private ImageView next, back;


    public static BooksAndStarsFragment newInstance() {
        Bundle args = new Bundle();

        BooksAndStarsFragment fragment = new BooksAndStarsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books_and_stars, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);

        initBooks(view);

        statsUtils = new StatsUtils(getParentFragmentManager(), getContext());

        next.setOnClickListener(v -> statsUtils.goTo(new BooksReadPerMonthFragment()));

        back.setOnClickListener(v -> statsUtils.goTo(new YourYearFragment()));

        changeYear(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeYear(View view) {

        yearET.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (yearET.getText().toString().matches(getString(R.string.regex_year))) {
                    initBooks(view);
                    return true;
                }
                if (getContext() != null)
                    Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.year_invalid) + "</b></font>"), Toast.LENGTH_LONG).show();

                return true;
            }
            return false;
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initBooks(View view) {
        RetrofitService retrofitService = new RetrofitService();
        StatsApi statsApi = retrofitService.getRetrofit().create(StatsApi.class);

        Integer year = Integer.parseInt(yearET.getText().toString());

        LocalDate now = LocalDate.now();
        if (year > now.getYear()) {
            if (getContext() != null) {
                Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.future) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }
            return;
        }


        statsApi.booksAndStars(year).enqueue(new Callback<Map<Integer, List<UserBook>>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Map<Integer, List<UserBook>>> call, Response<Map<Integer, List<UserBook>>> response) {
                if (response.isSuccessful()) {

                    if (response.body() == null) {
                        if (getContext() != null)
                            Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Map<Integer, List<UserBook>> map = response.body();

                    if (map.size() == 0) {
                        statsUtils.showWarningDialog(R.string.warning);
                        return;
                    }

                    setupStar(books5Stars, map.get(Utils.STAR5), no5);
                    setupStar(books4Stars, map.get(Utils.STAR4), no4);
                    setupStar(books3Stars, map.get(Utils.STAR3), no3);
                    setupStar(books2Stars, map.get(Utils.STAR2), no2);
                    setupStar(books1Stars, map.get(Utils.STAR1), no1);


                    return;
                }

                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<Map<Integer, List<UserBook>>> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setupStar(RecyclerView recyclerView, List<UserBook> userBooks, TextView textView) {
        setupBookAdapter(recyclerView, userBooks);
        if (userBooks.size() != 0 && getContext() != null) {
            textView.setText(userBooks.size() + getString(R.string.booksAndStart));
            return;
        }
        textView.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(View view) {
        books1Stars = view.findViewById(R.id.stars1);
        books1Stars.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        books1Stars.setHasFixedSize(true);

        books2Stars = view.findViewById(R.id.stars2);
        books2Stars.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        books2Stars.setHasFixedSize(true);

        books3Stars = view.findViewById(R.id.stars3);
        books3Stars.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        books3Stars.setHasFixedSize(true);

        books4Stars = view.findViewById(R.id.stars4);
        books4Stars.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        books4Stars.setHasFixedSize(true);

        books5Stars = view.findViewById(R.id.stars5);
        books5Stars.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        books5Stars.setHasFixedSize(true);

        no5 = view.findViewById(R.id.no5);
        no4 = view.findViewById(R.id.no4);
        no3 = view.findViewById(R.id.no3);
        no2 = view.findViewById(R.id.no2);
        no1 = view.findViewById(R.id.no1);


        yearET = view.findViewById(R.id.year);
        LocalDate now = LocalDate.now();
        yearET.setText(String.valueOf(now.getYear()));

        next = view.findViewById(R.id.nextPage);
        back = view.findViewById(R.id.back);
    }


    private void setupBookAdapter(RecyclerView recyclerView, List<UserBook> books) {

        UserBookAdapter bookAdapter = new UserBookAdapter(books);
        recyclerView.setAdapter(bookAdapter);
    }


}