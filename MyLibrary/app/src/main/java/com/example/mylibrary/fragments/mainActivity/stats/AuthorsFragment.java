package com.example.mylibrary.fragments.mainActivity.stats;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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
import com.example.mylibrary.activities.BookDetailsActivity;
import com.example.mylibrary.adapters.BookCallback;
import com.example.mylibrary.adapters.UserBookAdapter;
import com.example.mylibrary.api.StatsApi;
import com.example.mylibrary.api.UserBookApi;
import com.example.mylibrary.chartManager.PieChartManager;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Author;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.StatsUtils;
import com.example.mylibrary.utils.Utils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthorsFragment extends Fragment implements BookCallback {

    private ImageView back;
    private EditText yearET;
    private StatsUtils statsUtils;
    private PieChartManager authorsChart;
    private Author author;
    private CardView cardView;
    private RecyclerView booksRV;
    private List<UserBook> books;

    public static AuthorsFragment newInstance() {
        Bundle args = new Bundle();

        AuthorsFragment fragment = new AuthorsFragment();
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
        return inflater.inflate(R.layout.fragment_authors, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init(view);

        statsUtils = new StatsUtils(getParentFragmentManager(), getContext());

        initChart(view);
        initCardView();


        back.setOnClickListener(v -> statsUtils.goTo(new PublicationYearFragment()));

        changeYear(view);

        authorsChart.getPieChart().setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e.getData() != null) {
                    books = (List<UserBook>) e.getData();
                    setupBookAdapter(books);
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
                    initCardView();
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

            return;
        }


        statsApi.genAuthors(year).enqueue(new Callback<Map<String, List<UserBook>>>() {
            @Override
            public void onResponse(Call<Map<String, List<UserBook>>> call, Response<Map<String, List<UserBook>>> response) {
                if (response.isSuccessful()) {
                    Map<String, List<UserBook>> data = response.body();
                    if (data.size() == 0) {
                        statsUtils.showWarningDialog(R.string.warning);
                        return;
                    }

                    PieChart authors = view.findViewById(R.id.authors);
                    authorsChart = new PieChartManager(authors);
                    authorsChart.setAuthors(data);
                    authorsChart.setAuthors();
                    authorsChart.costume();
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
        back = view.findViewById(R.id.back);


        PieChart authors = view.findViewById(R.id.authors);
        authorsChart = new PieChartManager(authors);

        cardView = view.findViewById(R.id.favorite);

        booksRV = view.findViewById(R.id.books);
        booksRV.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        booksRV.setHasFixedSize(true);

        yearET = view.findViewById(R.id.year);
        LocalDate now = LocalDate.now();
        yearET.setText(String.valueOf(now.getYear()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initCardView() {
        RetrofitService retrofitService = new RetrofitService();
        StatsApi statsApi = retrofitService.getRetrofit().create(StatsApi.class);

        Integer year = Integer.parseInt(yearET.getText().toString());


        LocalDate now = LocalDate.now();
        if (year > now.getYear()) {
            return;
        }

        statsApi.favoriteAuthor(year).enqueue(new Callback<Author>() {
            @Override
            public void onResponse(Call<Author> call, Response<Author> response) {
                if (response.isSuccessful()) {
                    author = response.body();
                    if (author.getId() == null) {
                        return;
                    }
                    TextView name = cardView.findViewById(R.id.authorName);
                    ImageView img = cardView.findViewById(R.id.authorImg);

                    name.setText(author.getName());
                    Picasso.get().load(author.getPhoto()).into(img);
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<Author> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });

    }

    private void setupBookAdapter(List<UserBook> books) {

        UserBookAdapter bookAdapter = new UserBookAdapter(books, this);
        booksRV.setAdapter(bookAdapter);
    }

    @Override
    public void onBookItemClick(int pos) {
        Intent i = new Intent(this.requireContext(), BookDetailsActivity.class);
        i.putExtra("book", books.get(pos).getBook());
        startActivity(i);
        requireActivity().finish();
    }
}