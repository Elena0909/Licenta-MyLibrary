package com.example.mylibrary.chartManager;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.example.mylibrary.R;
import com.example.mylibrary.requestModel.UserBook;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.model.GradientColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HorizontalBarChartManager {
    private HorizontalBarChart horizontalBarChart;

    public HorizontalBarChart getHorizontalBarChart() {
        return horizontalBarChart;
    }

    public void setHorizontalBarChart(HorizontalBarChart horizontalBarChart) {
        this.horizontalBarChart = horizontalBarChart;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;


    private List<String> labelsX;

    public HorizontalBarChartManager(HorizontalBarChart horizontalBarChart, Context context) {
        this.horizontalBarChart = horizontalBarChart;
        this.context = context;
    }


    public Map<String, Integer> getGenres() {
        return genres;
    }

    public void setGenres(Map<String, Integer> genres) {
        this.genres = genres;
    }

    private Map<String, Integer> genres;


    public List<String> getLabelsX() {
        return labelsX;
    }

    public void setLabelsX(List<String> labelsX) {
        this.labelsX = labelsX;
    }


    public void initGraph() {
        horizontalBarChart.setDrawBarShadow(false);
        horizontalBarChart.setDrawValueAboveBar(false);


        horizontalBarChart.setPinchZoom(true);
        horizontalBarChart.setScaleEnabled(false);

        horizontalBarChart.setDrawGridBackground(false);

        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(labelsX);
        XAxis xAxis = horizontalBarChart.getXAxis();


        xAxis.setLabelCount(12, true);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setLabelCount(12);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.parseColor("#FFFFFFFF"));
        xAxis.setAxisLineColor(Color.parseColor("#FFFFFFFF"));


        YAxis rightAxis = horizontalBarChart.getAxisRight();
        rightAxis.setGranularity(1);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setTextSize(12f);
        rightAxis.setTextColor(Color.parseColor("#FFFFFFFF"));
        rightAxis.setAxisLineColor(Color.parseColor("#FFFFFFFF"));


        YAxis leftAxis = horizontalBarChart.getAxisLeft();
        leftAxis.setGranularity(1);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#FFFFFFFF"));
        leftAxis.setTextSize(12f);
        leftAxis.setAxisLineColor(Color.parseColor("#FFFFFFFF"));


        Legend l = horizontalBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setForm(Legend.LegendForm.NONE);
        l.setFormSize(15f);
        l.setTextSize(12);
        l.setXEntrySpace(0.8f);
        l.setTextColor(R.color.yellow);

        Description description = horizontalBarChart.getDescription();
        description.setText("2021");
        horizontalBarChart.setDescription(description);

        horizontalBarChart.invalidate();
        horizontalBarChart.refreshDrawableState();

    }


    public void setData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        BarDataSet set1;

        for (String genre : labelsX
        ) {
            entries.add(new BarEntry(labelsX.indexOf(genre), genres.get(genre)));
        }
        set1 = new BarDataSet(entries, "2021");
        set1.setColor(R.color.yellow);
        set1.setDrawIcons(false);
        set1.setDrawValues(false);
        //set color of bars
       set1.setColor(Color.parseColor("#009688"));

        BarData data = new BarData(set1);
        data.setDrawValues(false);
        data.setBarWidth(0.9f);
        horizontalBarChart.setData(data);

        horizontalBarChart.invalidate();
        horizontalBarChart.refreshDrawableState();
    }

}

