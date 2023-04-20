package com.example.mylibrary.api;

import com.example.mylibrary.requestModel.Author;
import com.example.mylibrary.requestModel.Book;
import com.example.mylibrary.requestModel.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BooksApi {

    @GET("/category/{name}/books/{page}")
    Call<List<Book>> getAllByCategory(@Path("name") String name, @Path("page") int page);

    @GET("/{search}/{page}")
    Call<List<Book>> search(@Path("search") String search,@Path("page") int page);

    @GET("/genre/{name}/books/{page}")
    Call<List<Book>> getAllByGenre(@Path("name") String name,@Path("page") int page);

    @GET("/author/books/{id}")
    Call<List<Book>> getAllByAuthorId(@Path("id") Long id);

    @GET("/series/books/{id}")
    Call<List<Book>> getAllBySeriesId(@Path("id") Long id);
}

