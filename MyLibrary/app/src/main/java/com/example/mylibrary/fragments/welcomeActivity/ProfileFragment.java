package com.example.mylibrary.fragments.welcomeActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mylibrary.R;
import com.example.mylibrary.activities.MainActivity;
import com.example.mylibrary.api.LoginApi;
import com.example.mylibrary.api.UserApi;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.User;
import com.example.mylibrary.utils.BitmapConverter;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.ImageManager;
import com.example.mylibrary.utils.Utils;

import java.io.IOException;
import java.util.Base64;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    public static final String TAG_FRAGMENT_PROFILE = "TAG_FRAGMENT_PROFILE";

    private OnFragmentActivityCommunication activityCommunication;

    private ImageView userIcon;
    private TextView username, skipTV, changePhoto;
    private EditText name, description;
    private Button saveButton;
    private ImageManager imageManager;
    private User user;


    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        imageManager = new ImageManager(this, userIcon);
        changePhoto.setOnClickListener(v -> imageManager.selectImage());

        skip();

        saveButton.setOnClickListener(v -> save());
    }

    private void skip() {
        skipTV.setOnClickListener(v -> goToMainActivity());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUser() {
        user = Utils.getUser();

        user.setName(name.getText().toString());
        user.setDescription(description.getText().toString());
        Bitmap bitmap = ((BitmapDrawable) userIcon.getDrawable()).getBitmap();
        byte[] bytes = BitmapConverter.fromBitmap(bitmap);
        String image = Base64.getEncoder().encodeToString(bytes);
        user.setImage(image);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void save() {

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        setUser();

        userApi.update(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Utils.setUser(user);
                    goToMainActivity();
                    return;
                }
                ErrorManager.getErrorMessage(getContext(), response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ErrorManager.failCall(getContext(),t);
            }
        });

    }

    private void goToMainActivity() {
        Intent i = new Intent(this.requireContext(), MainActivity.class);
        i.putExtra("status", 0L);
        startActivity(i);
        requireActivity().finish();
    }

    private void init(View view) {

        username = view.findViewById(R.id.username);
        username.setText(Utils.getUsername());

        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);

        saveButton = view.findViewById(R.id.save);
        skipTV = view.findViewById(R.id.skip);
        changePhoto = view.findViewById(R.id.changePhoto);

        userIcon = view.findViewById(R.id.userIcon);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentActivityCommunication) {
            activityCommunication = (OnFragmentActivityCommunication) context;
        }
    }

}
