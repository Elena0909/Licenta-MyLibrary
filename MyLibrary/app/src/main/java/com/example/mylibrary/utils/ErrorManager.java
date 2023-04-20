package com.example.mylibrary.utils;

import android.content.Context;
import android.text.Html;
import android.widget.Toast;

import com.example.mylibrary.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ErrorManager {

    public static void getErrorMessage(Context context, Response<?> response){
        if(response.errorBody()!=null && context!=null)
            try {
                switch (response.code()) {
                    case 400:
                        Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.error_400) + response.errorBody().string() + "</b></font>"), Toast.LENGTH_LONG).show();
                        break;
                    case 401:
                        Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.error_401) + "</b></font>"), Toast.LENGTH_LONG).show();
                        break;
                    case 404:
                        Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" +  context.getString(R.string.error_404) + response.errorBody().string() + "</b></font>"), Toast.LENGTH_LONG).show();
                        break;
                    case 409:
                        Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" + context.getString(R.string.error_409) + response.errorBody().string() + "</b></font>"), Toast.LENGTH_LONG).show();
                        return;
                    default:
                        Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" +response.code()+ " error: " + response.errorBody().string() + "</b></font>"), Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
    }

    public static void failCall(Context context, Throwable throwable){
        if(throwable!=null && context!=null){
            if(throwable.getMessage().contains("failed to connect to")) {
                Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" +  context.getString(R.string.error_server) + "</b></font>"), Toast.LENGTH_LONG).show();
                System.out.println(context.getString(R.string.call_fail) + throwable.getMessage());
                return;
            }
            if(throwable.getMessage().contains("timeout")) {
                Toast.makeText(context, Html.fromHtml("<font color='#9E0000' ><b>" +  context.getString(R.string.timeout) + "</b></font>"), Toast.LENGTH_LONG).show();
                System.out.println(context.getString(R.string.call_fail) + throwable.getMessage());
                return;
            }

            System.out.println(context.getString(R.string.call_fail) + throwable.getMessage());
        }
    }
}
