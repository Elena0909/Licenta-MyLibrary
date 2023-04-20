package com.example.mylibrary.api;

import com.example.mylibrary.requestModel.Author;
import com.example.mylibrary.requestModel.UserBook;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StatsApi {
    @GET("/stats/booksAndStars/{year}")
    Call<Map<Integer, List<UserBook>>> booksAndStars(@Path("year") Integer year);

    @GET("/stats/booksReadPerYear/{year}")
    Call<List<UserBook>> booksReadPerYear(@Path("year") Integer year);

    @GET("/stats/booksReadPerMonth/{year}")
    Call<Map<String, List<UserBook>>> booksReadPerMonth(@Path("year") Integer year);

    @GET("/stats/pagesReadPerMonth/{year}")
    Call<Map<String, Integer>> pagesReadPerMonth(@Path("year") Integer year);

    @GET("/stats/slowestBook/{year}")
    Call<UserBook> slowestBook(@Path("year") Integer year);

    @GET("/stats/fastestBook/{year}")
    Call<UserBook> fastestBook(@Path("year") Integer year);

    @GET("/stats/longestBook/{year}")
    Call<UserBook> longestBook(@Path("year") Integer year);

    @GET("/stats/shortestBook/{year}")
    Call<UserBook> shortestBook(@Path("year") Integer year);

    @GET("/stats/yourYear/{year}")
    Call<Map<String, Double>> yourYear(@Path("year") Integer year);

    @GET("/stats/booksGenre/{year}")
    Call<Map<String, Integer>> booksGenre(@Path("year") Integer year);

    @GET("/stats/bookCategories/{year}")
    Call<Map<String, Integer>> bookCategories(@Path("year") Integer year);

    @GET("/stats/bookFormats/{year}")
    Call<Map<String, Integer>> bookFormats(@Path("year") Integer year);

    @GET("/stats/genAuthors/{year}")
    Call<Map<String, List<UserBook>>> genAuthors(@Path("year") Integer year);

    @GET("/stats/favoriteAuthor/{year}")
    Call<Author> favoriteAuthor(@Path("year") Integer year);

    @GET("/stats/getAllBooksRead/{year}")
    Call<Map<String, List<UserBook>>> getAllBooksRead(@Path("year") Integer year);

}
