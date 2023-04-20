package com.example.mylibrary.fragments.welcomeActivity;

import android.content.Context;
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
import com.example.mylibrary.api.LoginApi;
import com.example.mylibrary.api.UserApi;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Login;
import com.example.mylibrary.requestModel.User;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {
    public static final String TAG_FRAGMENT_REGISTER = "TAG_FRAGMENT_REGISTER";

    private OnFragmentActivityCommunication activityCommunication;

    private EditText username, password, confPassword;
    private Button registerButton;
    private TextView goToLogin;


    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        register();

        goToLogin.setOnClickListener(v -> {
            if (activityCommunication != null) {
                activityCommunication.onAddFragment(LoginFragment.TAG_FRAGMENT_LOGIN);
            }
        });
    }

    private void register() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateAccount())
                    return;

                RetrofitService retrofitService = new RetrofitService();
                LoginApi loginApi = retrofitService.getRetrofit().create(LoginApi.class);

                Login login = new Login(username.getText().toString(), password.getText().toString());

                loginApi.register(login).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String token = response.body();
                            Utils.setToken(token);
                            Utils.setUsername(username.getText().toString());
                            getUserDetails();

                        } else {
                            try {
                                String error = response.errorBody().string();
                                switch (error) {
                                    case "Username can not be null!":
                                    case "Username already exists!": {
                                        username.setError(error);
                                        username.findFocus();
                                        break;
                                    }
                                    case "Password can not be null!":
                                    case "Choose a strong password, min 6 characters": {
                                        password.setError(error);
                                        password.findFocus();
                                        break;
                                    }
                                    default: {
                                        Toast.makeText(getContext(), getString(R.string.error_500) + error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(getContext(), getString(R.string.error_500) + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
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
                    goToProfile();
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


    private void goToProfile() {
        if (activityCommunication != null) {
            activityCommunication.onAddFragment(ProfileFragment.TAG_FRAGMENT_PROFILE);
        }
    }

    private void init(View view) {

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        confPassword = view.findViewById(R.id.confirm_password);

        registerButton = view.findViewById(R.id.register);

        goToLogin = view.findViewById(R.id.go_to_login);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentActivityCommunication) {
            activityCommunication = (OnFragmentActivityCommunication) context;
        }
    }

    private boolean validateAccount() {

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

        if (password.getText().toString().length() < 6) {
            password.setError("Choose a strong password, min 6 characters");
            password.requestFocus();
            return false;
        }

        if (confPassword.getText().toString().isEmpty()) {
            confPassword.setError("Confirm your password");
            confPassword.requestFocus();
            return false;
        }

        if (!confPassword.getText().toString().equals(password.getText().toString())) {
            confPassword.setError("Passwords don't match");
            confPassword.requestFocus();
            return false;
        }

        return true;
    }


}