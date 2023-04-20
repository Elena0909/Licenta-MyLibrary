package com.example.mylibrary.api;

import com.example.mylibrary.requestModel.Login;
import com.example.mylibrary.requestModel.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserApi {

    @PUT("/user/update")
    Call<User> update(@Body User user);

    @GET("user/getUserDetails")
    Call<User> getUserDetails();
}
