package com.example.mylibrary.fragments.mainActivity.stats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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
import com.example.mylibrary.api.StatsApi;
import com.example.mylibrary.api.UserBookApi;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.StatsUtils;
import com.example.mylibrary.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourYearFragment extends Fragment {

    private ImageView next;
    private EditText yearET;
    private StatsUtils statsUtils;
    private TextView books, pages, avgBooks, avgStars, avgPages;

    public static YourYearFragment newInstance() {
        Bundle args = new Bundle();

        YourYearFragment fragment = new YourYearFragment();
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
        return inflater.inflate(R.layout.fragment_your_year, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init(view);
        setStats(view);

        statsUtils = new StatsUtils(getParentFragmentManager(), getContext());

        next.setOnClickListener(v -> statsUtils.goTo(new BooksAndStarsFragment()));

        changeYear(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeYear(View view) {

        yearET.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (yearET.getText().toString().matches(getString(R.string.regex_year))){
                    setStats(view);
                return true;}
                if(getContext()!=null)
                    Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.year_invalid) + "</b></font>"), Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setStats(View view) {
        RetrofitService retrofitService = new RetrofitService();
        StatsApi statsApi = retrofitService.getRetrofit().create(StatsApi.class);
        int year = Integer.parseInt(yearET.getText().toString());

        LocalDate now = LocalDate.now();
        if (year > now.getYear()) {
            if (getContext() != null) {
                Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.future) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }

            return;
        }

        statsApi.yourYear(year).enqueue(new Callback<Map<String, Double>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Map<String, Double>> call, Response<Map<String, Double>> response) {
                if (response.isSuccessful()) {

                    if (response.body() == null) {
                        if (getContext() != null)
                            Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Map<String, Double> stats = response.body();


                    if (stats.size() == 0) {
                        statsUtils.showWarningDialog(R.string.warning);
                        return;
                    }


                    DecimalFormat i = new DecimalFormat("#0");
                    DecimalFormat dec = new DecimalFormat("#0.00");


                    if (getContext() != null) {
                        books.setText(getString(R.string.books_read) + " " + i.format(stats.get("books")));
                        avgBooks.setText(getString(R.string.avg_books) + " " + dec.format(stats.get("avgBooks")) + "/month");
                        avgStars.setText(getString(R.string.avg_stars) + " " + dec.format(stats.get("avgStars")) + "/book");
                        pages.setText(getString(R.string.pages_read) + " " + i.format(stats.get("pages")));
                        avgPages.setText(getString(R.string.avg_pages) + " " + dec.format(stats.get("avgPages")) + "/book");
                    }
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }


            @Override
            public void onFailure(Call<Map<String, Double>> call, Throwable t) {
                ErrorManager.failCall(getContext(),t);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(View view) {
        next = view.findViewById(R.id.next);

        yearET = view.findViewById(R.id.year);
        LocalDate now = LocalDate.now();
        yearET.setText(String.valueOf(now.getYear()));

        books = view.findViewById(R.id.books);
        avgBooks = view.findViewById(R.id.avg_books);
        avgStars = view.findViewById(R.id.avg_stars);
        pages = view.findViewById(R.id.pages);
        avgPages = view.findViewById(R.id.avg_pages);
    }


}