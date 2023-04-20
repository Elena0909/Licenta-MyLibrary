package com.example.mylibrary.reotrfit;

import com.example.mylibrary.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;


    public RetrofitService() {
        initRetrofit();
    }

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private void initRetrofit() {
        if(Utils.getToken()==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.8:8081")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return;
        }
        updateRetrofit(Utils.getToken());
    }

    public void updateRetrofit(String token) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization","Bearer "+ token)
                    .build();
            return chain.proceed(newRequest);
        }).build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://192.168.1.8:8081")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}