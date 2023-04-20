package com.example.mylibrary.fragments.bookDetailsActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.activities.BookDetailsActivity;
import com.example.mylibrary.adapters.BookCallback;
import com.example.mylibrary.adapters.UserBookAdapter;
import com.example.mylibrary.api.UserBookApi;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.User;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.BitmapConverter;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.Status;
import com.example.mylibrary.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfileFragment extends Fragment implements BookCallback {

    private TextView name, description;
    private RecyclerView books;
    private ImageView userImg;
    private User user;
    private boolean lastPage = false;
    private Long status = Status.WANT;
    private Button want, current, read;
    private UserBookAdapter bookAdapter;
    private int page = Utils.FIRST_PAGE;
    private List<UserBook> userBooks = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = this.getArguments();
        try {
            assert bundle != null;
            user = (User) bundle.getSerializable("user");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }


        initView(view);
        setView();

        initBooks(Status.WANT, view);

        want.setOnClickListener(v -> {
            if (status != Status.WANT) {
                lastPage = false;
                page = Utils.FIRST_PAGE;
            }
            initBooks(Status.WANT, v);
        });

        current.setOnClickListener(v -> {
            if (status != Status.CURRENT) {
                lastPage = false;
                page = Utils.FIRST_PAGE;
            }
            initBooks(Status.CURRENT, v);
        });

        read.setOnClickListener(v -> {
            if (status != Status.READ) {
                lastPage = false;
                page = Utils.FIRST_PAGE;
            }
            initBooks(Status.READ, v);
        });

        books.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollHorizontally(1) && dx > 0) {
                    if (lastPage) {
                        Snackbar.make(view, "Last books", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    Snackbar.make(view, "Loading...", Snackbar.LENGTH_LONG).show();

                    initBooks(status, view);
                    return;
                }

                if (!recyclerView.canScrollHorizontally(-1) && dx < 0) {
                    Snackbar.make(view, "First", Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setColor(Button select, Button other1, Button other2) {
        if (getContext() != null) {
            select.setBackgroundColor(getContext().getResources().getColor(R.color.select_yellow));
            other1.setBackgroundColor(getContext().getResources().getColor(R.color.yellow));
            other2.setBackgroundColor(getContext().getResources().getColor(R.color.yellow));
        }
    }

    private void initBooks(Long idStatus, View view) {


        RetrofitService retrofitService = new RetrofitService();
        UserBookApi userBookApi = retrofitService.getRetrofit().create(UserBookApi.class);

        userBookApi.getUserBooks(user.getId(), idStatus, page).enqueue(new Callback<List<UserBook>>() {
            @Override
            public void onResponse(Call<List<UserBook>> call, Response<List<UserBook>> response) {
                if (!response.isSuccessful()) {
                    ErrorManager.getErrorMessage(getContext(),response);
                    return;
                }
                if (response.body() == null) {
                    if (getContext() != null)
                        Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                    return;
                }

                if (!status.equals(idStatus) || page == Utils.FIRST_PAGE) {

                    userBooks.clear();
                    userBooks = response.body();
                    status = idStatus;
                    page++;
                    if (userBooks.size() == 0) {
                        Snackbar.make(view, "List is empty", Snackbar.LENGTH_LONG).show();
                    }

                    if (idStatus.equals(Status.WANT))
                        setColor(want, current, read);
                    if (idStatus.equals(Status.CURRENT))
                        setColor(current, want, read);
                    if (idStatus.equals(Status.READ))
                        setColor(read, current, want);

                    setupBookAdapter();
                    return;
                }
                if (response.body().isEmpty()) {
                    lastPage = true;
                    Snackbar.make(view, "Last books", Snackbar.LENGTH_LONG).show();
                    return;
                }

                bookAdapter.addBooks(response.body(), page);
                page++;
            }

            @Override
            public void onFailure(Call<List<UserBook>> call, Throwable t) {
                ErrorManager.failCall(getContext(),t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setView() {
        name.setText(user.getName());
        if (user.getDescription() != null)
            description.setText(user.getDescription());

        if (!user.getImage().equals("")) {
            byte[] decode = Base64.getDecoder().decode(user.getImage());
            Bitmap bitmap = BitmapConverter.fromBytes(decode);
            userImg.setImageBitmap(bitmap);
        }
    }

    private void setupBookAdapter() {
        bookAdapter = new UserBookAdapter(userBooks, this);
        books.setAdapter(bookAdapter);
    }

    private void initView(View view) {
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);
        books = view.findViewById(R.id.rvBooks);

        books.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        books.setHasFixedSize(true);

        userImg = view.findViewById(R.id.userIcon);

        want = view.findViewById(R.id.want);
        current = view.findViewById(R.id.current);
        read = view.findViewById(R.id.read);


    }

    @Override
    public void onBookItemClick(int pos) {
        Intent i = new Intent(this.requireContext(), BookDetailsActivity.class);
        i.putExtra("book", userBooks.get(pos).getBook());
        startActivity(i);
        requireActivity().finish();
    }
}