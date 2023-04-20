package com.example.mylibrary.api;

import com.example.mylibrary.requestModel.Login;
import com.example.mylibrary.requestModel.User;
import com.example.mylibrary.requestModel.UserBook;

import kotlin.ParameterName;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginApi {

    @POST("/auth/login")
    Call<String> login(@Body Login login);


    @POST("/auth/register")
    Call<String> register(@Body Login login);
}
