package com.example.mylibrary.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mylibrary.api.UserApi;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.User;

import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    private static String username;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Utils.user = user;
    }

    private static User user;

    public static final int STAR1 = 1;
    public static final int STAR2 = 2;
    public static final int STAR3 = 3;
    public static final int STAR4 = 4;
    public static final int STAR5 = 5;

    public static final int FIRST_PAGE = 0;
    public static final int SEARCH = 0;
    public static final int CATEGORY = 1;
    public static final int GENRE = 2;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Utils.username = username;
    }

    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Utils.token = token;
    }

    public static final List<String> MONTHS = Arrays.asList("Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun.", "Jul.", "Aug.", "Sep", "Oct.", "Nov.", "Dec.");

    public Utils() {

    }

    public static void logout() {
        token = "";
        user = null;
        username = "";
    }
}
