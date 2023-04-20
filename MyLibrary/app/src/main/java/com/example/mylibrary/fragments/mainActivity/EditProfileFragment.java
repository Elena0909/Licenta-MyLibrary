package com.example.mylibrary.fragments.mainActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.Base64;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    public static final String TAG_FRAGMENT_EDIT_PROFILE = "TAG_FRAGMENT_EDIT_PROFILE";

    private OnFragmentActivityCommunication activityCommunication;

    private TextView username, nameTV, descriptionTV,giveUp;
    private EditText nameET, descriptionET;
    private ImageView changePhoto, userIcon;
    private Button updateButton, editButton;
    private ImageManager imageManager;
    private User user;

    public static EditProfileFragment newInstance() {
        Bundle args = new Bundle();

        EditProfileFragment fragment = new EditProfileFragment();
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = Utils.getUser();

        init(view);
        setView();


        editButton.setOnClickListener(v -> edit());

        updateButton.setOnClickListener(v -> update());

        giveUp.setOnClickListener(v->updateView());

        imageManager = new ImageManager(this, userIcon);

        changePhoto.setOnClickListener(v -> imageManager.selectImage());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setView() {


        nameTV.setText(user.getName());
        descriptionTV.setText(user.getDescription());
        username.setText(Utils.getUsername());

        if (!user.getImage().isEmpty()) {
            byte[] decode = Base64.getDecoder().decode(user.getImage());
            Bitmap bitmap = BitmapConverter.fromBytes(decode);
            userIcon.setImageBitmap(bitmap);
        }
    }

    private void edit() {
        nameET.setVisibility(View.VISIBLE);
        descriptionET.setVisibility(View.VISIBLE);
        changePhoto.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.VISIBLE);
        giveUp.setVisibility(View.VISIBLE);

        nameTV.setVisibility(View.GONE);
        descriptionTV.setVisibility(View.GONE);

        editButton.setVisibility(View.GONE);

        nameET.setText(nameTV.getText().toString());
        descriptionET.setText(descriptionTV.getText().toString());
    }

    private void updateView() {
        nameET.setVisibility(View.GONE);
        descriptionET.setVisibility(View.GONE);
        changePhoto.setVisibility(View.GONE);
        updateButton.setVisibility(View.GONE);
        giveUp.setVisibility(View.GONE);

        nameTV.setVisibility(View.VISIBLE);
        descriptionTV.setVisibility(View.VISIBLE);

        editButton.setVisibility(View.VISIBLE);

        nameTV.setText(user.getName());
        descriptionTV.setText(user.getDescription());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUser() {
        user.setName(nameET.getText().toString());
        user.setDescription(descriptionET.getText().toString());

        if (user.getImage() != "") {
            Bitmap bitmap = ((BitmapDrawable) userIcon.getDrawable()).getBitmap();
            byte[] bytes = BitmapConverter.fromBitmap(bitmap);
            String image = Base64.getEncoder().encodeToString(bytes);
            user.setImage(image);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void update() {
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        setUser();

        userApi.update(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    if (user == null) {
                        if (getContext() != null)
                            Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Utils.setUser(user);

                    NavigationView navigationView = getActivity().findViewById(R.id.navigationView);
                    View headerView = navigationView.getHeaderView(0);
                    TextView navUsername = (TextView) headerView.findViewById(R.id.username);
                    navUsername.setText(Utils.getUsername());
                    ImageView userIcon = headerView.findViewById(R.id.icon);


                    if (!user.getImage().equals("")) {
                        byte[] decode = Base64.getDecoder().decode(user.getImage());
                        Bitmap bitmap = BitmapConverter.fromBytes(decode);
                        userIcon.setImageBitmap(bitmap);
                    }
                    updateView();
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


    private void init(View view) {

        username = view.findViewById(R.id.username);
        username.setText(Utils.getUsername());

        nameTV = view.findViewById(R.id.nameTV);
        descriptionTV = view.findViewById(R.id.descriptionTV);

        nameET = view.findViewById(R.id.nameET);
        descriptionET = view.findViewById(R.id.descriptionET);

        updateButton = view.findViewById(R.id.update);
        editButton = view.findViewById(R.id.edit);
        giveUp = view.findViewById(R.id.giveUp);

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
