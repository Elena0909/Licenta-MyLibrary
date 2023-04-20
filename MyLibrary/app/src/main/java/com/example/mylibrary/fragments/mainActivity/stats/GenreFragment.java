package com.example.mylibrary.fragments.mainActivity.stats;

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
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.api.StatsApi;
import com.example.mylibrary.api.UserBookApi;
import com.example.mylibrary.chartManager.HorizontalBarChartManager;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.StatsUtils;
import com.example.mylibrary.utils.Utils;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreFragment extends Fragment {

    private HorizontalBarChartManager horizontalBarChartManager;
    StatsUtils statsUtils;
    private ImageView back, next;
    private EditText yearET;

    public static GenreFragment newInstance() {
        Bundle args = new Bundle();

        GenreFragment fragment = new GenreFragment();
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
        return inflater.inflate(R.layout.fragment_genre, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init(view);

        initChart(view);

        statsUtils = new StatsUtils(getParentFragmentManager(), getContext());


        next.setOnClickListener(v -> statsUtils.goTo(new CategoryAndFormatBookFragment()));

        back.setOnClickListener(v -> statsUtils.goTo(new BookFragment()));

        changeYear(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeYear(View view) {

        yearET.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (yearET.getText().toString().matches(getString(R.string.regex_year))) {

                    initChart(view);
                    return true;
                }
                if(getContext()!=null)
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

        Integer year = Integer.parseInt(yearET.getText().toString());

        LocalDate now = LocalDate.now();
        if (year > now.getYear()) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.future) + "</b></font>"), Toast.LENGTH_LONG).show();
                    return;
                }
        }
        statsApi.booksGenre(year).enqueue(new Callback<Map<String, Integer>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful()) {
                    Map<String, Integer> dataChart = response.body();

                    if (dataChart.size()==0) {
                        statsUtils.showWarningDialog(R.string.warning);
                        return;
                    }

                    List<String> labelX = new ArrayList<>();
                    dataChart.forEach((s, aDouble) -> labelX.add(s));
                    horizontalBarChartManager.setLabelsX(labelX);
                    horizontalBarChartManager.setGenres(dataChart);
                    horizontalBarChartManager.initGraph();
                    horizontalBarChartManager.setData();
                    return;
                }
                ErrorManager.getErrorMessage(getContext(),response);
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                ErrorManager.failCall(getContext(),t);
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

        HorizontalBarChart horizontalBarChart = view.findViewById(R.id.genres);
        horizontalBarChartManager = new HorizontalBarChartManager(horizontalBarChart, getContext());
    }

}