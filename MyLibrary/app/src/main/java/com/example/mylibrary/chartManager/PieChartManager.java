package com.example.mylibrary.chartManager;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mylibrary.R;
import com.example.mylibrary.requestModel.UserBook;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PieChartManager {
    public PieChart getPieChart() {
        return pieChart;
    }

    private PieChart pieChart;
    List<PieEntry> value;

    public Map<String, Integer> getData() {
        return data;
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
    }

    private Map<String, Integer> data;

    public Map<String, List<UserBook>> getAuthors() {
        return authors;
    }

    public void setAuthors(Map<String, List<UserBook>> authors) {
        this.authors = authors;
    }

    private Map<String, List<UserBook>> authors;
    public static final List<Integer> colors = Arrays.asList(Color.parseColor("#ffcb4e"), Color.parseColor("#9E0000"), Color.parseColor("#CD5100"), Color.parseColor("#009688"), Color.parseColor("#613213"),Color.parseColor("#2A0088"));


    public PieChartManager(PieChart pieChart) {
        this.pieChart = pieChart;
        value = new ArrayList<>();
        Collections.shuffle(colors);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData(){
        data.forEach((s, integer) -> {
            if (integer != 0)
                value.add(new PieEntry(integer, s + "\n" + integer));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAuthors(){
        authors.forEach((s, userBooks) -> {
            if(userBooks.size()!=0)
                value.add(new PieEntry(userBooks.size(), s + "\n" + userBooks.size(), userBooks));
        });
    }

    public void costume() {
        PieDataSet pieDataSet = new PieDataSet(value, "");
        pieDataSet.setValueTextColor(Color.parseColor("#FFFFFFFF"));
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(14);
        pieData.setValueTextColor(Color.parseColor("#FFFFFFFF"));


        pieDataSet.setValueLinePart1OffsetPercentage(110.f);
        pieDataSet.setValueLinePart1Length(1.f);
        pieDataSet.setValueLinePart2Length(.1f);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setEntryLabelTextSize(16);


        Legend legend = pieChart.getLegend();
        legend.setFormSize(0.5f);
        legend.setForm(Legend.LegendForm.NONE);

        Description description = pieChart.getDescription();
        description.setText("");
        pieChart.setDescription(description);


        pieChart.invalidate();
        pieChart.refreshDrawableState();
    }


}
