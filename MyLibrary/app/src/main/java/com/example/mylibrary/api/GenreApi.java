package com.example.mylibrary.api;

import com.example.mylibrary.requestModel.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GenreApi {

    @GET("/genres")
    Call<List<Genre>> allGenres();


}
