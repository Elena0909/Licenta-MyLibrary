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
import com.example.mylibrary.api.StatsApi;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.StatsUtils;
import com.example.mylibrary.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFragment extends Fragment {


    private ImageView next, back;
    private EditText yearET;
    StatsUtils statsUtils;
    private CardView cardLongest, cardShortest, cardFastest, cardSlowest;

    public static BookFragment newInstance() {
        Bundle args = new Bundle();

        BookFragment fragment = new BookFragment();
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
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);

        RetrofitService retrofitService = new RetrofitService();
        StatsApi statsApi = retrofitService.getRetrofit().create(StatsApi.class);

        statsUtils = new StatsUtils(getParentFragmentManager(), getContext());


        LocalDate now = LocalDate.now();
        yearET.setText(String.valueOf(now.getYear()));
        Integer year = now.getYear();

        initCardLongest(statsApi, year);
        initCardShortest(statsApi, year);
        initCardFastest(statsApi, year);
        initCardSlowest(statsApi, year);

        next.setOnClickListener(v -> statsUtils.goTo(new GenreFragment()));

        back.setOnClickListener(v -> statsUtils.goTo(new BooksReadPerMonthFragment()));

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

                        return true;
                    }

                    initCards(statsApi, year);
                    return true;
                }

                if(getContext()!=null)
                    Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.year_invalid) + "</b></font>"), Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        });

    }

    private void initCards(StatsApi statsApi, Integer year) {
        initCardLongest(statsApi, year);
        initCardShortest(statsApi, year);
        initCardFastest(statsApi, year);
        initCardSlowest(statsApi, year);
    }


    private void initCardFastest(StatsApi statsApi, Integer year) {


        statsApi.fastestBook(year).enqueue(new Callback<UserBook>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<UserBook> call, Response<UserBook> response) {
                if (response.isSuccessful()) {

                    if (response.body() == null) {
                        if (getContext() != null)
                            Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                        return;
                    }

                    UserBook userBook = response.body();

                    if (userBook.getBook() == null) {
                        return;
                    }

                    long duration = ChronoUnit.DAYS.between(userBook.getDateStart().convertToLocalDate(), userBook.getDateFinished().convertToLocalDate())+ 1;
                    initCardView(cardFastest, userBook, "you read the book in " + duration + " days");
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<UserBook> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });
    }

    private void initCardSlowest(StatsApi statsApi, Integer year) {


        statsApi.slowestBook(year).enqueue(new Callback<UserBook>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<UserBook> call, Response<UserBook> response) {
                if (response.isSuccessful()) {
                    UserBook userBook = response.body();


                    if (userBook.getBook() == null) {
                        return;
                    }

                    Long duration = ChronoUnit.DAYS.between(userBook.getDateStart().convertToLocalDate(), userBook.getDateFinished().convertToLocalDate()) + 1;
                    initCardView(cardSlowest, userBook, "you read the book in " + duration + " days");
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<UserBook> call, Throwable t) {
                if (getContext() != null)
                    System.out.println(getString(R.string.call_fail) + t.getMessage());
            }
        });
    }

    private void initCardShortest(StatsApi statsApi, Integer year) {

        statsApi.shortestBook(year).enqueue(new Callback<UserBook>() {
            @Override
            public void onResponse(Call<UserBook> call, Response<UserBook> response) {
                if (response.isSuccessful()) {
                    UserBook userBook = response.body();

                    if (userBook == null) {
                        return;
                    }

                    if (userBook.getBook() == null) {
                        return;
                    }

                    String text = userBook.getBook().getPages() + " pages";
                    initCardView(cardShortest, userBook, text);
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<UserBook> call, Throwable t) {
                if (getContext() != null)
                    System.out.println(getString(R.string.call_fail) + t.getMessage());
            }
        });
    }

    private void initCardLongest(StatsApi statsApi, Integer year) {

        statsApi.longestBook(year).enqueue(new Callback<UserBook>() {
            @Override
            public void onResponse(Call<UserBook> call, Response<UserBook> response) {
                if (response.isSuccessful()) {
                    UserBook userBook = response.body();

                    if (userBook.getBook() == null) {
                        statsUtils.showWarningDialog(R.string.warning);
                        return;
                    }

                    String text = userBook.getBook().getPages() + " pages";
                    initCardView(cardLongest, userBook, text);
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<UserBook> call, Throwable t) {
                if (getContext() != null)
                    System.out.println(getString(R.string.call_fail) + t.getMessage());
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(View view) {
        next = view.findViewById(R.id.next);
        back = view.findViewById(R.id.back);

        yearET = view.findViewById(R.id.year);


        cardSlowest = view.findViewById(R.id.cardSlowest);
        cardShortest = view.findViewById(R.id.cardShortest);
        cardFastest = view.findViewById(R.id.cardFastest);
        cardLongest = view.findViewById(R.id.cardLongest);

    }


    private void initCardView(CardView cardView, UserBook userBook, String text) {
        if (userBook != null) {
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