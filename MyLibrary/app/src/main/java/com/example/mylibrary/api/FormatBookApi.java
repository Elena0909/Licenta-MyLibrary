package com.example.mylibrary.api;

import com.example.mylibrary.requestModel.FormatBook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FormatBookApi {
    @GET("/format/all")
    Call<List<FormatBook>> all();
}
