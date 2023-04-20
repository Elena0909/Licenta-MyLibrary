package com.example.mylibrary.fragments.bookDetailsActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.activities.BookDetailsActivity;
import com.example.mylibrary.activities.MainActivity;
import com.example.mylibrary.api.FormatBookApi;
import com.example.mylibrary.api.UserBookApi;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Book;
import com.example.mylibrary.requestModel.FormatBook;
import com.example.mylibrary.requestModel.MyDate;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.Status;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadBookFragment extends Fragment {

    public static final String TAG_FRAGMENT_READ_BOOK = "TAG_FRAGMENT_READ_BOOK";

    private OnFragmentActivityCommunication activityCommunication;

    private Book book;

    private List<FormatBook> formatBooks;

    private RatingBar rating;
    private ImageView imgBook, imgDateStart, imgDateFinish;
    private TextView title, authorTV, dateStartTV, dateFinishTV;
    private EditText review;
    private Button cancel, save;
    private LocalDate dateStart, dateFinish;
    private Spinner spinner;

    private FormatBook formatBook;
    private UserBook userBook;


    public static ReadBookFragment newInstance() {
        Bundle args = new Bundle();

        ReadBookFragment fragment = new ReadBookFragment();
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
        return inflater.inflate(R.layout.fragment_read_book, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        try {
            assert bundle != null;
            book = (Book) bundle.getSerializable("book");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }


        init(view);
        initUserBook();
        setView();

        setDate(imgDateStart, dateStartTV, true);
        setDate(imgDateFinish, dateFinishTV, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String format = parent.getItemAtPosition(position).toString();
                if (format.equals("Set format")) {
                    formatBook = null;
                    return;
                }

                formatBook = formatBooks.stream().filter(formatBook1 -> formatBook1.getName().equals(format)).findFirst().get();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel.setOnClickListener(v -> goBack());
        save.setOnClickListener(this::validation);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showInfoDialog(String message, View view) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_info);

        TextView info = dialog.findViewById(R.id.info);
        Button cancel, save;
        cancel = dialog.findViewById(R.id.cancel);
        save = dialog.findViewById(R.id.save);

        info.setText(message);

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        save.setOnClickListener(v -> {
            readBook();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void goBack() {
        Intent i = new Intent(this.requireContext(), BookDetailsActivity.class);
        i.putExtra("book", book);
        startActivity(i);
        requireActivity().finish();
    }

    private void initFormatList() {
        RetrofitService retrofitService = new RetrofitService();
        FormatBookApi formatBookApi = retrofitService.getRetrofit().create(FormatBookApi.class);

        formatBookApi.all().enqueue(new Callback<List<FormatBook>>() {
            @Override
            public void onResponse(Call<List<FormatBook>> call, Response<List<FormatBook>> response) {
                if (response.isSuccessful()) {
                    formatBooks = response.body();
                    List<String> formatList = new ArrayList<>();
                    formatList.add("Set format");

                    for (FormatBook formatBook : formatBooks) {
                        formatList.add(formatBook.getName());
                    }

                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, formatList);

                    spinner.setAdapter(adapter);
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<List<FormatBook>> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDate(ImageView dateIV, TextView dateTV, boolean startOrEnd) {

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateIV.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), (view, year1, month1, day1) -> {
                month1 += 1;
                String dateString = day1 + "." + month1 + "." + year1;
                final LocalDate date = LocalDate.of(year1, month1, day1);
                LocalDate now = LocalDate.now();
                if (date.compareTo(now) > 0) {
                    if(getContext()!=null)
                    Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" +  getString(R.string.future) + "</b></font>"), Toast.LENGTH_LONG).show();

                    return;
                }
                if (startOrEnd) {
                    dateStart = date;
                    dateStartTV.setVisibility(View.VISIBLE);
                } else {
                    dateFinish = date;
                    dateFinishTV.setVisibility(View.VISIBLE);
                }
                dateTV.setText(dateString);
            }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void setView() {
        Picasso.get().load(book.getPhoto()).into(imgBook);
        title.setText(book.getName());
        String author = book.getAuthors().get(0).getName();

        if (book.getAuthors().size() != 1)
            author += "...";

        authorTV.setText(author);

        LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getContext(), R.color.yellow), PorterDuff.Mode.SRC_ATOP);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUserBook() {
        if (userBook != null) {
            if(userBook.getStatusId().equals(Status.READ))
            {
                if(getContext()!=null)
                    Toast.makeText(getContext(), Html.fromHtml("<font color='#00695C' ><b>" + getString(R.string.book_read) + "</b></font>"), Toast.LENGTH_LONG).show();
            }
            if (userBook.getDateStart() != null) {
                dateStart = userBook.getDateStart().convertToLocalDate();
                dateStartTV.setVisibility(View.VISIBLE);
                dateStartTV.setText(userBook.getDateStart().toString());
            }
            if (userBook.getDateFinished() != null) {
                dateFinish = userBook.getDateFinished().convertToLocalDate();
                dateFinishTV.setVisibility(View.VISIBLE);
                dateFinishTV.setText(userBook.getDateFinished().toString());
            }
            if (userBook.getFormatBookId() != null) {
                spinner.setSelection(userBook.getFormatBookId().intValue());
                for (FormatBook f : formatBooks
                ) {
                    if (f.getId().equals(userBook.getFormatBookId())) {
                        formatBook = f;
                        break;
                    }
                }
            }
            if (userBook.getReview() != null)
                review.setText(userBook.getReview());

            if (userBook.getStars() != null)
                rating.setRating(userBook.getStars());
        }
    }

    @SuppressLint("SetTextI15n")
    private void showSuccessMessage(String message, Long status) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_info);

        TextView info = dialog.findViewById(R.id.info);
        Button cancel, goToMyBooks;
        cancel = dialog.findViewById(R.id.cancel);
        goToMyBooks = dialog.findViewById(R.id.save);
        goToMyBooks.setText("My books");

        info.setText(message);

        cancel.setOnClickListener(v ->
                dialog.dismiss()
        );

        goToMyBooks.setOnClickListener(v -> {
            goToMyBooks(status);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void goToMyBooks(Long status) {
        Intent i = new Intent(this.requireContext(), MainActivity.class);
        i.putExtra("status", status);
        startActivity(i);
        requireActivity().finish();
    }

    private void initUserBook() {
        RetrofitService retrofitService = new RetrofitService();
        UserBookApi userBookApi = retrofitService.getRetrofit().create(UserBookApi.class);


        userBookApi.checkExist(book.getId()).enqueue(new Callback<UserBook>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<UserBook> call, @NonNull Response<UserBook> response) {
                if (response.isSuccessful()) {
                    userBook = response.body();
                    setUserBook();
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

    private void init(View view) {
        rating = view.findViewById(R.id.rating);
        imgDateStart = view.findViewById(R.id.img_dateStart);
        imgDateFinish = view.findViewById(R.id.img_dateFinish);
        dateStartTV = view.findViewById(R.id.dataStart);
        dateFinishTV = view.findViewById(R.id.dataFinish);
        review = view.findViewById(R.id.review);
        cancel = view.findViewById(R.id.cancel);
        save = view.findViewById(R.id.save);
        spinner = view.findViewById(R.id.spinner);

        initFormatList();

        imgBook = view.findViewById(R.id.img);
        title = view.findViewById(R.id.title);
        authorTV = view.findViewById(R.id.author);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void readBook() {

        if (userBook == null) {
            userBook = new UserBook();
            userBook.setBook(book);
        }

        userBook.setDateStart(new MyDate(dateStart));
        userBook.setDateFinished(new MyDate(dateFinish));
        userBook.setFormatBookId(formatBook.getId());
        userBook.setReview(review.getText().toString());
        userBook.setStatusId(Status.READ);
        userBook.setStars(rating.getRating());

        RetrofitService retrofitService = new RetrofitService();
        UserBookApi userBookApi = retrofitService.getRetrofit().create(UserBookApi.class);

        userBookApi.create(userBook).enqueue(new Callback<UserBook>() {
            @Override
            public void onResponse(@NonNull Call<UserBook> call, @NonNull Response<UserBook> response) {
                if (response.isSuccessful()) {
                    userBook = response.body();
                    showSuccessMessage("your list of read books has been updated", Status.READ);
                    //Snackbar.make(view, "your list of read books has been updated", Snackbar.LENGTH_LONG).show();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validation(View view) {
        Context context = getContext();
        if (context != null) {
            LocalDate now = LocalDate.now();

            if (dateStart == null) {
                Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.missing_start_date) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }

            if (dateFinish == null) {
                Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.missing_end_date) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }

            if (dateStart.compareTo(now) > 0) {
                Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.future) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }

            if (dateFinish.compareTo(now) > 0) {
                Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.future) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }


            if (dateFinish.compareTo(dateStart) < 0) {
                Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.end_befor_start) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }
            if (rating.getRating() == 0) {
                Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.must_ranting) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }

            if (formatBook == null) {
                Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.must_format) + "</b></font>"), Toast.LENGTH_LONG).show();
                return;
            }

            if (review.getText().toString().isEmpty()) {
                showInfoDialog("Save without review?", view);
                return;
            }
            readBook();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentActivityCommunication) {
            activityCommunication = (OnFragmentActivityCommunication) context;
        }
    }


}