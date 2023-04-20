package com.example.mylibrary.fragments.bookDetailsActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.activities.MainActivity;
import com.example.mylibrary.adapters.AuthorAdapter;
import com.example.mylibrary.adapters.AuthorCallback;
import com.example.mylibrary.adapters.BookCallback;
import com.example.mylibrary.adapters.BookReviewAdapter;
import com.example.mylibrary.adapters.CoverAdapter;
import com.example.mylibrary.adapters.UserCallback;
import com.example.mylibrary.api.BooksApi;
import com.example.mylibrary.api.UserBookApi;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Author;
import com.example.mylibrary.requestModel.Book;
import com.example.mylibrary.requestModel.Genre;
import com.example.mylibrary.requestModel.MyDate;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.Status;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookDetailsFragment extends Fragment implements BookCallback, AuthorCallback, UserCallback {

    public static final String TAG_FRAGMENT_BOOK_DETAILS = "TAG_FRAGMENT_BOOK_DETAILS";

    private OnFragmentActivityCommunication activityCommunication;

    private Dialog dialog;

    private int page = Utils.FIRST_PAGE;
    private boolean lastPage = false;
    private BookReviewAdapter bookReviewAdapter;

    private Book book;
    private List<UserBook> reviews;
    private List<Book> books;
    private List<Author> authors;

    private long currentStatus;

    private ImageView img;
    private TextView title, authorTV, pages, description, genres, category, date, series;
    private RatingBar stars;
    private RecyclerView rvBooks, rvReviews;
    private Button want, current, read;

    public static BookDetailsFragment newInstance() {
        Bundle args = new Bundle();

        BookDetailsFragment fragment = new BookDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_book_details, container, false);
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

        authors = book.getAuthors();

        init(view);
        setView();

        authorTV.setOnClickListener(v -> {
            Fragment fragment = new AuthorDetailsFragment();
            Bundle bundle1 = new Bundle();
            if (book.getAuthors().size() == 1) {
                bundle1.putSerializable("author", book.getAuthors().get(0));
                fragment.setArguments(bundle1);
                getParentFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
                return;
            }
            showAuthorsInfoDialog();
        });


        want.setOnClickListener(v -> {
            if(currentStatus== Status.READ){
                showInfoMessage("This book is on your list of read books, if you move it to another list the reading data will be lost.","want");
            return;
            }
            chooseStatues("want");
        });

        current.setOnClickListener(v -> {
            if(currentStatus== Status.READ){
                showInfoMessage("This book is on your list of read books, if you move it to another list the reading data will be lost.","current");
                return;
            }
            chooseStatues("current");
        });
        read.setOnClickListener(v -> chooseStatues("read"));


        rvReviews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    if (lastPage) {
                        Snackbar.make(view, "Last review", Snackbar.LENGTH_LONG).show();

                        return;
                    }
                    Snackbar.make(view, "Loading...", Snackbar.LENGTH_LONG).show();

                    initReviews(view);

                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    Snackbar.make(view, "First", Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showInfoMessage(String message, String status) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_info);

        TextView info = dialog.findViewById(R.id.info);
        Button cancel, save;
        cancel = dialog.findViewById(R.id.cancel);
        save = dialog.findViewById(R.id.save);

        info.setText(message);
        info.setTextSize(16f);

        cancel.setOnClickListener(v ->
                dialog.dismiss()
        );

       save.setOnClickListener(v -> {
           chooseStatues(status);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void currentStatus() {

        RetrofitService retrofitService = new RetrofitService();
        UserBookApi userBookApi = retrofitService.getRetrofit().create(UserBookApi.class);

        userBookApi.getCurrentStatus(book.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        System.out.println(getString(R.string.object_null));
                        return;
                    }
                    String status = response.body();
                    setCurrentStatus(status);
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });
    }

    private void setCurrentStatus(String status) {
        switch (status) {
            case "want":
                currentStatus = Status.WANT;
                setColor(want, current, read);
                break;
            case "current":
                currentStatus = Status.CURRENT;
                setColor(current, want, read);
                break;
            case "read":
                currentStatus = Status.READ;
                setColor(read, want, current);
                break;
        }
    }

    private void setColor(Button select, Button other1, Button other2) {
        if (getContext() != null) {
            select.setBackgroundColor(getContext().getResources().getColor(R.color.select_yellow));
            other1.setBackgroundColor(getContext().getResources().getColor(R.color.yellow));
            other2.setBackgroundColor(getContext().getResources().getColor(R.color.yellow));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void chooseStatues(String choose) {
        UserBook userBook = new UserBook();
        userBook.setBook(book);


        switch (choose) {
            case "want": {
                userBook.setStatusId(Status.WANT);
                break;
            }
            case "current": {
                LocalDate localDate = LocalDate.now();
                MyDate dateStart = new MyDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
                userBook.setDateStart(dateStart);
                userBook.setStatusId(Status.CURRENT);
                break;
            }
            case "read":
                Fragment fragment = new ReadBookFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", (Serializable) book);
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
                return;
            default:
                if(getContext()!=null)
                Toast.makeText(getContext(), "Error choose status", Toast.LENGTH_LONG).show();
                return;
        }

        RetrofitService retrofitService = new RetrofitService();
        UserBookApi userBookApi = retrofitService.getRetrofit().create(UserBookApi.class);


        userBookApi.create(userBook).enqueue(new Callback<UserBook>() {
            @Override
            public void onResponse(@NonNull Call<UserBook> call, @NonNull Response<UserBook> response) {
                if (response.isSuccessful()) {
                    UserBook userBook = response.body();
                    if (userBook == null) {
                        System.out.println(getString(R.string.object_null));
                        return;
                    }
                    showSuccessMessage("Your " + choose + " list has been updated", userBook.getStatusId());
                    //Snackbar.make(view, "Your " + choose + " list has been updated", Snackbar.LENGTH_LONG).show();
                    setCurrentStatus(choose);
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


    private void initReviews(View view) {
        RetrofitService retrofitService = new RetrofitService();
        UserBookApi userBookApi = retrofitService.getRetrofit().create(UserBookApi.class);


        userBookApi.reviewsBook(book.getId(), page).enqueue(new Callback<List<UserBook>>() {
            @Override
            public void onResponse(Call<List<UserBook>> call, Response<List<UserBook>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        System.out.println(getString(R.string.object_null));
                        return;
                    }

                    if (response.body().isEmpty()) {
                        if (page == Utils.FIRST_PAGE) {
                            rvReviews.setVisibility(View.GONE);
                            return;
                        }
                        lastPage = true;
                        Snackbar.make(view, "Last review", Snackbar.LENGTH_LONG).show();

                        return;
                    }

                    if (page == Utils.FIRST_PAGE) {
                        reviews = response.body();
                        setupReviewsAdapter();
                        page++;
                        return;
                    }

                    bookReviewAdapter.addReview(response.body(), page);
                    page++;
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

    private void setupReviewsAdapter() {
        bookReviewAdapter = new BookReviewAdapter(reviews, this);
        rvReviews.setAdapter(bookReviewAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void init(View view) {
        img = view.findViewById(R.id.img);
        title = view.findViewById(R.id.title);
        authorTV = view.findViewById(R.id.author);
        authorTV.setPaintFlags(authorTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        pages = view.findViewById(R.id.pages);
        date = view.findViewById(R.id.date);
        stars = view.findViewById(R.id.ratingBar);
        description = view.findViewById(R.id.description);
        category = view.findViewById(R.id.category);
        genres = view.findViewById(R.id.genres);
        rvBooks = view.findViewById(R.id.rvBooksS);
        series = view.findViewById(R.id.series);
        rvReviews = view.findViewById(R.id.rvReviews);
        LinearLayoutManager linearLayoutManagerR = new LinearLayoutManager(getContext());
        linearLayoutManagerR.setOrientation(LinearLayoutManager.VERTICAL);
        rvReviews.setLayoutManager(linearLayoutManagerR);

        initReviews(view);

        if (book.getVolume() != null) {

            series.setText(book.getSeries().getName() + ", volume " + book.getVolume());


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getLayoutInflater().getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);

            rvBooks.setLayoutManager(linearLayoutManager);
            rvBooks.setHasFixedSize(true);
            initBookList();
        } else {
            TextView s = view.findViewById(R.id.s);
            s.setVisibility(View.GONE);
            rvBooks.setVisibility(View.GONE);
            series.setVisibility(View.GONE);
        }

        want = view.findViewById(R.id.want);
        current = view.findViewById(R.id.current);
        read = view.findViewById(R.id.read);
        currentStatus();
    }

    private void initBookList() {

        RetrofitService retrofitService = new RetrofitService();
        BooksApi booksApi = retrofitService.getRetrofit().create(BooksApi.class);

        booksApi.getAllBySeriesId(book.getSeries().getId()).enqueue(new Callback<List<Book>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        System.out.println(getString(R.string.object_null));
                        return;
                    }
                    books = response.body().stream().filter(b -> !b.getVolume().equals(book.getVolume())).collect(Collectors.toList());
                    setupBookAdapter();
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                ErrorManager.failCall(getContext(), t);
            }
        });

    }

    private void setupBookAdapter() {
        rvBooks.setAdapter(new CoverAdapter(books, this));
    }

    private void setView() {

        Picasso.get().load(book.getPhoto()).into(img);
        title.setText(book.getName());
        pages.setText(String.valueOf(book.getPages()));

        String author = book.getAuthors().get(0).getName();

        if (book.getAuthors().size() != 1)
            author += "...";

        description.setText(book.getSynopsis());
        authorTV.setText(author);
        date.setText(book.getPublicationDate().toString());
        category.setText(book.getCategory().getName());

        StringBuilder genres = new StringBuilder();
        for (Genre genre : book.getGenres()
        ) {
            genres.append(genre.getName()).append(" ");
        }
        this.genres.setText(genres.toString());
        stars.setRating(book.getStars());
    }

    private void showAuthorsInfoDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_authors);

        RecyclerView authorsRV = dialog.findViewById(R.id.authorRV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        authorsRV.setLayoutManager(linearLayoutManager);

        AuthorAdapter adapter = new AuthorAdapter(authors, this);
        authorsRV.setAdapter(adapter);

        dialog.show();
    }

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentActivityCommunication) {
            activityCommunication = (OnFragmentActivityCommunication) context;
        }
    }

    @Override
    public void onBookItemClick(int pos) {
        Fragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", (Serializable) books.get(pos));
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
    }

    @Override
    public void onAuthorItemClick(int pos) {
        dialog.dismiss();
        Fragment fragment = new AuthorDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("author", (Serializable) book.getAuthors().get(pos));
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
    }

    @Override
    public void onUserReviewItemClick(int pos) {
        if (!Utils.getUser().getId().equals(reviews.get(pos).getUser().getId())) {
            Fragment fragment = new UserProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", (Serializable) reviews.get(pos).getUser());
            fragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
        }
    }


}