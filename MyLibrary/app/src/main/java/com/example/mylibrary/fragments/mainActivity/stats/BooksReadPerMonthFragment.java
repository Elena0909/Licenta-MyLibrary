package com.example.mylibrary.fragments.mainActivity.stats;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.adapters.UserBookAdapter;
import com.example.mylibrary.api.StatsApi;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.chartManager.BarChartManager;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.StatsUtils;
import com.example.mylibrary.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksReadPerMonthFragment extends Fragment {

    private RecyclerView booksRV;
    private BarChartManager barChartManager;
    private Button booksB, pagesB;
    private Map<String, List<UserBook>> books;
    private Map<String, Integer> pages;
    private TextView info;
    private String monthSelected;
    private ImageView back, next;
    private EditText yearET;
    private StatsUtils statsUtils;


    public static BooksReadPerMonthFragment newInstance() {
        Bundle args = new Bundle();

        BooksReadPerMonthFragment fragment = new BooksReadPerMonthFragment();
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
        return inflater.inflate(R.layout.fragment_books_read_per_month, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init(view);

        statsUtils = new StatsUtils(getParentFragmentManager(), getContext());

        Integer year = Integer.parseInt(yearET.getText().toString());

        initBooks(year);


        booksB.setOnClickListener(v -> {
            if (books != null)
                booksClick();
        });


        pagesB.setOnClickListener(v -> {
            if (books != null && pages != null)
                pagesClick();
        });

        next.setOnClickListener(v -> statsUtils.goTo(new BookFragment()));

        back.setOnClickListener(v -> statsUtils.goTo(new BooksAndStarsFragment()));

        changeYear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeYear() {

        yearET.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (yearET.getText().toString().matches(getString(R.string.regex_year))) {
                    Integer year = Integer.parseInt(yearET.getText().toString());

                    LocalDate now = LocalDate.now();
                    if (year > now.getYear()) {
                            if (getContext() != null) {
                                Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.future) + "</b></font>"), Toast.LENGTH_LONG).show();
                                return true;
                            }

                        return true;
                    }

                    initBooks(year);
                    return true;
                }
                if (getContext() != null)
                    Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.year_invalid) + "</b></font>"), Toast.LENGTH_LONG).show();

                return true;
            }
            return false;
        });
    }


    private void pagesClick() {
        if (monthSelected != null) {
            String text = monthSelected + ": " + pages.get(monthSelected) + " pages read.";
            info.setText(text);
            List<UserBook> selectedBooks = books.get(monthSelected);
            setupBookAdapter(selectedBooks);
        }

        if (getContext() != null) {
            booksB.setBackgroundColor(getContext().getResources().getColor(R.color.yellow));
            pagesB.setBackgroundColor(getContext().getResources().getColor(R.color.select_yellow));
        }

        barChartManager.setPages(pages);
        barChartManager.setDataPages();

        barChartManager.getBarChart().setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                List<UserBook> selectedBooks = books.get(Utils.MONTHS.get((int) e.getX()));
                monthSelected = Utils.MONTHS.get((int) e.getX());
                String text = monthSelected + ": " + pages.get(monthSelected) + " pages read.";
                info.setText(text);
                setupBookAdapter(selectedBooks);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void booksClick() {
        if (monthSelected != null) {
            String text = monthSelected + ": " + books.get(monthSelected).size() + " books read.";
            info.setText(text);
            List<UserBook> selectedBooks = books.get(monthSelected);
            setupBookAdapter(selectedBooks);
        }

        if (getContext() != null) {
            booksB.setBackgroundColor(getContext().getResources().getColor(R.color.select_yellow));
            pagesB.setBackgroundColor(getContext().getResources().getColor(R.color.yellow));
        }

        barChartManager.setBooks(books);
        barChartManager.setDataBooks();


        barChartManager.getBarChart().setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                List<UserBook> selectedBooks = books.get(Utils.MONTHS.get((int) e.getX()));
                monthSelected = Utils.MONTHS.get((int) e.getX());
                String text = monthSelected + ": " + books.get(monthSelected).size() + " books read.";
                info.setText(text);
                setupBookAdapter(selectedBooks);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void initPages(Integer year) {
        RetrofitService retrofitService = new RetrofitService();
        StatsApi statsApi = retrofitService.getRetrofit().create(StatsApi.class);

        statsApi.pagesReadPerMonth(year).enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful()) {
                    pages = response.body();
                    if (pages.size() == 0) {
                        statsUtils.showWarningDialog(R.string.warning);
                        return;
                    }
                    pagesClick();
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initBooks(Integer year) {
        RetrofitService retrofitService = new RetrofitService();
        StatsApi statsApi = retrofitService.getRetrofit().create(StatsApi.class);



        statsApi.booksReadPerMonth(year).enqueue(new Callback<Map<String, List<UserBook>>>() {
            @Override
            public void onResponse(Call<Map<String, List<UserBook>>> call, Response<Map<String, List<UserBook>>> response) {
                if (response.isSuccessful()) {
                    books = response.body();
                    initPages(year);
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<Map<String, List<UserBook>>> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(View view) {
        booksRV = view.findViewById(R.id.books);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        booksRV.setLayoutManager(gridLayoutManager);

        BarChart vBarChart = view.findViewById(R.id.VBarChart);
        barChartManager = new BarChartManager(vBarChart, Utils.MONTHS);
        barChartManager.initGraph();
        info = view.findViewById(R.id.info);

        booksB = view.findViewById(R.id.booksB);
        pagesB = view.findViewById(R.id.pagesB);

        next = view.findViewById(R.id.next);
        back = view.findViewById(R.id.back);

        yearET = view.findViewById(R.id.year);
        LocalDate now = LocalDate.now();
        yearET.setText(String.valueOf(now.getYear()));

    }

    private void setupBookAdapter(List<UserBook> books) {
        UserBookAdapter bookAdapter = new UserBookAdapter(books);
        booksRV.setAdapter(bookAdapter);
    }


}