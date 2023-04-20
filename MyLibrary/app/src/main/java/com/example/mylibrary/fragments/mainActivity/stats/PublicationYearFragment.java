package com.example.mylibrary.fragments.mainActivity.stats;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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
import com.example.mylibrary.chartManager.ScatterChartManager;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.StatsUtils;
import com.example.mylibrary.utils.Utils;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicationYearFragment extends Fragment {

    private ImageView back, next;
    private EditText yearET;
    private ScatterChartManager scatterChartManager;
    private CardView bookCardView;
    private ScatterChart scatterChart;
    private StatsUtils statsUtils;


    public static PublicationYearFragment newInstance() {
        Bundle args = new Bundle();

        PublicationYearFragment fragment = new PublicationYearFragment();
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
        return inflater.inflate(R.layout.fragment_publication_year, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init(view);
        statsUtils = new StatsUtils(getParentFragmentManager(), getContext());

        initChart(view);

        next.setOnClickListener(v -> statsUtils.goTo(new AuthorsFragment()));

        back.setOnClickListener(v -> statsUtils.goTo(new CategoryAndFormatBookFragment()));

        changeYear(view);

        scatterChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                System.out.println(e.getData());
                if (e.getData() != null) {
                    UserBook userBook = (UserBook) e.getData();
                    initCardView(bookCardView, userBook, "" +
                            "The book was published on " + userBook.getBook().getPublicationDate() + " and you read it on " + userBook.getDateFinished());
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeYear(View view) {

        yearET.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (yearET.getText().toString().matches(getString(R.string.regex_year))) {
                    initChart(view);
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
    private void initChart(View view) {

        RetrofitService retrofitService = new RetrofitService();
        StatsApi statsApi = retrofitService.getRetrofit().create(StatsApi.class);

        int year = Integer.parseInt(yearET.getText().toString());

        LocalDate now = LocalDate.now();
        if (year > now.getYear()) {
            if (getContext() != null) {
                Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.future) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }
        }

        statsApi.booksReadPerYear(year).enqueue(new Callback<List<UserBook>>() {
            @Override
            public void onResponse(Call<List<UserBook>> call, Response<List<UserBook>> response) {
                if (response.isSuccessful()) {
                    List<UserBook> books = response.body();

                    if (books.size() == 0) {
                        statsUtils.showWarningDialog(R.string.warning);
                        return;
                    }

                    scatterChartManager = new ScatterChartManager(scatterChart, books);
                    scatterChartManager.init();

                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<List<UserBook>> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(View view) {
        next = view.findViewById(R.id.next);
        back = view.findViewById(R.id.back);

        yearET = view.findViewById(R.id.year);
        LocalDate now = LocalDate.now();
        yearET.setText(String.valueOf(now.getYear()));

        scatterChart = view.findViewById(R.id.publication);


        bookCardView = view.findViewById(R.id.book);
    }


    private void initCardView(CardView cardView, UserBook userBook, String text) {
        if (userBook != null) {
            cardView.setVisibility(View.VISIBLE);
            TextView title = cardView.findViewById(R.id.title);
            title.setText(userBook.getBook().getName());

            TextView info = cardView.findViewById(R.id.info);
            info.setText(text);

            TextView author = cardView.findViewById(R.id.author);
            author.setText(userBook.getBook().getAuthors().get(0).getName());
            ImageView img = cardView.findViewById(R.id.img);
            Picasso.get().load(userBook.getBook().getPhoto()).into(img);
        }
    }
}