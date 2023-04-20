package com.example.mylibrary.requestModel;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;

public class MyDate implements Serializable {
    private int year;
    private int month;
    private int day;

    public MyDate() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MyDate(LocalDate localDate) {
        this.year = localDate.getYear();
        this.month = localDate.getMonthValue();
        this.day = localDate.getDayOfMonth();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate convertToLocalDate(){
        return LocalDate.of(this.year,this.month,this.day);
    }

    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return day + "." + month + "." + year;
    }
}
