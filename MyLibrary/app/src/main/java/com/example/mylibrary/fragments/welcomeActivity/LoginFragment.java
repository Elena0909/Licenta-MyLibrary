package com.example.mylibrary.fragments.welcomeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.activities.MainActivity;
import com.example.mylibrary.api.LoginApi;
import com.example.mylibrary.api.UserApi;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Login;
import com.example.mylibrary.requestModel.User;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.Utils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    public static final String TAG_FRAGMENT_LOGIN = "TAG_FRAGMENT_LOGIN";
    private OnFragmentActivityCommunication activityCommunication;

    private EditText username, password;
    private Button loginButton;
    private TextView goToRegister;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        login();

        goToRegister.setOnClickListener(v -> {
            if (activityCommunication != null) {
                activityCommunication.onAddFragment(RegisterFragment.TAG_FRAGMENT_REGISTER);
            }
        });
    }

    private void init(View view) {

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.login);
        goToRegister = view.findViewById(R.id.go_to_register);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentActivityCommunication) {
            activityCommunication = (OnFragmentActivityCommunication) context;
        }
    }

    private void goToMainActivity() {
        Intent i = new Intent(this.requireContext(), MainActivity.class);
        i.putExtra("status", 0L);
        startActivity(i);
        requireActivity().finish();
    }

    private boolean validateUsernameAndPassword() {

        if (username.getText().toString().isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return false;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return false;
        }

        return true;
    }

    private void login() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateUsernameAndPassword())
                    return;

                RetrofitService retrofitService = new RetrofitService();
                LoginApi loginApi = retrofitService.getRetrofit().create(LoginApi.class);

                Login login = new Login(username.getText().toString(), password.getText().toString());


                loginApi.login(login).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String token = response.body();
                            Utils.setToken(token);
                            Utils.setUsername(username.getText().toString());
                            getUserDetails();
                            return;
                        }
                        ErrorManager.getErrorMessage(getContext(),response);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        ErrorManager.failCall(getContext(),t);
                    }
                });

            }
        });
    }

    public void getUserDetails() {
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getUserDetails().enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Utils.setUser(response.body());
                    goToMainActivity();
                    return;
                }
                ErrorManager.getErrorMessage(getContext(),response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ErrorManager.failCall(getContext(),t);
            }
        });
    }
}