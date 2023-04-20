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
import com.example.mylibrary.chartManager.PieChartManager;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.StatsUtils;
import com.example.mylibrary.utils.Utils;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryAndFormatBookFragment extends Fragment {

    private ImageView back, next;
    private EditText yearET;
    private StatsUtils statsUtils;
    private PieChartManager categoriesChart, formatChart;


    public static CategoryAndFormatBookFragment newInstance() {
        Bundle args = new Bundle();

        CategoryAndFormatBookFragment fragment = new CategoryAndFormatBookFragment();
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
        return inflater.inflate(R.layout.fragment_category_and_format_book, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init(view);

        statsUtils = new StatsUtils(getParentFragmentManager(), getContext());

        next.setOnClickListener(v -> statsUtils.goTo(new PublicationYearFragment()));

        back.setOnClickListener(v -> statsUtils.goTo(new GenreFragment()));

        RetrofitService retrofitService = new RetrofitService();
        StatsApi statsApi = retrofitService.getRetrofit().create(StatsApi.class);

        LocalDate now = LocalDate.now();
        initCategories(statsApi, view, now.getYear());
        initFormat(statsApi, view, now.getYear());


        changeYear(view, statsApi);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeYear(View view, StatsApi statsApi) {

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
                    }


                    initCategories(statsApi, view, year);
                    initFormat(statsApi, view, year);
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
    private void initCategories(StatsApi statsApi, View view, Integer year) {

        statsApi.bookCategories(year).enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful()) {
                    PieChart categories = view.findViewById(R.id.categories);
                    Map<String, Integer> data = response.body();


                    if (data.size() == 0) {
                        statsUtils.showWarningDialog(R.string.warning);
                        return;
                    }

                    categoriesChart = new PieChartManager(categories);
                    categoriesChart.setData(data);
                    categoriesChart.setData();
                    categoriesChart.costume();
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
    private void initFormat(StatsApi statsApi, View view, Integer year) {

        statsApi.bookFormats(year).enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful()) {
                    PieChart formats = view.findViewById(R.id.formats);
                    Map<String, Integer> data = response.body();

                    if (data.size() == 0) {
                        return;
                    }

                    formatChart = new PieChartManager(formats);
                    formatChart.setData(data);
                    formatChart.setData();
                    formatChart.costume();
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
    private void init(View view) {
        next = view.findViewById(R.id.next);
        back = view.findViewById(R.id.back);

        yearET = view.findViewById(R.id.year);
        LocalDate now = LocalDate.now();
        yearET.setText(String.valueOf(now.getYear()));
    }

}