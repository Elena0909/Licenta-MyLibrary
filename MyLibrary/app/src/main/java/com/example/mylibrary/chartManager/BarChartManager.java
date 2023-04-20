package com.example.mylibrary.chartManager;

import android.graphics.Color;

import com.example.mylibrary.R;
import com.example.mylibrary.requestModel.UserBook;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BarChartManager {
    private BarChart barChart;


    private List<String> labelsX;

    public BarChartManager(BarChart barChart, List<String> labelsX) {
        this.barChart = barChart;
        this.labelsX = labelsX;
    }


    private Map<String, List<UserBook>> books;

    public Map<String, Integer> getPages() {
        return pages;
    }

    public void setPages(Map<String, Integer> pages) {
        this.pages = pages;
    }

    private Map<String, Integer> pages;

    public BarChart getBarChart() {
        return barChart;
    }

    public void setBarChart(BarChart barChart) {
        this.barChart = barChart;
    }

    public List<String> getLabelsX() {
        return labelsX;
    }

    public void setLabelsX(List<String> labelsX) {
        this.labelsX = labelsX;
    }

    public Map<String, List<UserBook>> getBooks() {
        return books;
    }

    public void setBooks(Map<String, List<UserBook>> books) {
        this.books = books;
    }

    public void initGraph() {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);


        barChart.setPinchZoom(true);
        barChart.setScaleEnabled(false);

        barChart.setDrawGridBackground(false);

        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(labelsX);
        XAxis xAxis = barChart.getXAxis();


        xAxis.setLabelCount(12, true);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setLabelCount(12);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(11f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.parseColor("#FFFFFFFF"));
        xAxis.setAxisLineColor(Color.parseColor("#FFFFFFFF"));


        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setGranularity(1);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setTextSize(12f);
        rightAxis.setTextColor(Color.parseColor("#FFFFFFFF"));
        rightAxis.setAxisLineColor(Color.parseColor("#FFFFFFFF"));


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#FFFFFFFF"));
        leftAxis.setTextSize(12f);
        leftAxis.setAxisLineColor(Color.parseColor("#FFFFFFFF"));


        Legend l = barChart.getLegend();
        l.setForm(Legend.LegendForm.NONE);
        l.setFormSize(0.5f);


        Description description = barChart.getDescription();
        description.setText("");
        barChart.setDescription(description);

        barChart.invalidate();
        barChart.refreshDrawableState();

    }

    public void setDataBooks() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        BarDataSet set1;

        for (String month : labelsX
        ) {
            entries.add(new BarEntry(labelsX.indexOf(month), books.get(month).size()));
        }
        set1 = new BarDataSet(entries, "2021");
        set1.setColor(R.color.yellow);
        set1.setDrawIcons(false);
        set1.setDrawValues(false);
        //set color of bars
        set1.setColor(Color.parseColor("#ffcb4e"));

        BarData data = new BarData(set1);
        data.setDrawValues(false);
        data.setBarWidth(0.9f);
        barChart.setData(data);

        barChart.invalidate();
        barChart.refreshDrawableState();
    }

    public void setDataPages() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        BarDataSet set1;

        for (String month : labelsX
        ) {
            entries.add(new BarEntry(labelsX.indexOf(month), pages.get(month)));
        }
        set1 = new BarDataSet(entries, "2021");
        set1.setColor(R.color.yellow);
        set1.setDrawIcons(false);
        set1.setDrawValues(false);
        //set color of bars
        set1.setColor(Color.parseColor("#ffcb4e"));

        BarData data = new BarData(set1);
        data.setDrawValues(false);
        data.setBarWidth(0.9f);
        barChart.setData(data);

        barChart.invalidate();
        barChart.refreshDrawableState();
    }

}
