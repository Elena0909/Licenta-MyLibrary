package com.example.mylibrary.api;

import com.example.mylibrary.requestModel.Author;
import com.example.mylibrary.requestModel.Nationality;
import com.example.mylibrary.requestModel.User;
import com.example.mylibrary.requestModel.UserBook;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserBookApi {

    @POST("/userBook/change")
    Call<UserBook> create(@Body UserBook userBook);

    @GET("/userBook/getFilteredBooks/{idStatus}/{page}")
    Call<List<UserBook>> getFilteredBooks(@Path("idStatus") Long idStatus, @Path("page") int page);

    @GET("/userBook/currentStatus/{idBook}")
    Call<String> getCurrentStatus(@Path("idBook") Long idBook);

    @GET("/userBook/checkExist/{idBook}")
    Call<UserBook> checkExist(@Path("idBook") Long idBook);

    @GET("/userBook/reviewsBook/{idBook}/{page}")
    Call<List<UserBook>> reviewsBook(@Path("idBook") Long idBook, @Path("page") int page);

    @GET("/userBook/getUserBooks/{idUser}/{idBook}/{page}")
    Call<List<UserBook>> getUserBooks(@Path("idUser") Long idUser,@Path("idBook") Long idBook, @Path("page") int page);

}
