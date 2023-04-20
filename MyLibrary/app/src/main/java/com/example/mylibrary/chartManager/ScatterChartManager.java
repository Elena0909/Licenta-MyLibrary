package com.example.mylibrary.chartManager;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.Utils;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScatterChartManager {
    public ScatterChart getScatterChart() {
        return scatterChart;
    }

    private ScatterChart scatterChart;
    private List<UserBook> data;


    public ScatterChartManager(ScatterChart scatterChart, List<UserBook> data) {
        this.scatterChart = scatterChart;
        this.data = data;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init() {

        List<Entry> scatterEntries = new ArrayList<>();
        List<String> months = new ArrayList<>();
        List<String> years = new ArrayList<>();

        for (UserBook userBook : data
        ) {
            months.add(userBook.getDateFinished().getDay() + " " + Utils.MONTHS.get(userBook.getDateFinished().getMonth() - 1));
            years.add(String.valueOf(userBook.getBook().getPublicationDate().getYear()));
            scatterEntries.add(new BarEntry(data.indexOf(userBook), userBook.getBook().getPublicationDate().getYear(), userBook));
        }

        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(months);
        XAxis xAxis = scatterChart.getXAxis();

        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextColor(Color.parseColor("#FFFFFFFF"));
        xAxis.setAxisLineColor(Color.parseColor("#FFFFFFFF"));
        xAxis.setGranularity(1.5f);
        xAxis.setTextSize(12);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        ValueFormatter yAxisFormatter = new IndexAxisValueFormatter(years);
        YAxis rightAxis = scatterChart.getAxisRight();
        rightAxis.setValueFormatter(yAxisFormatter);
        rightAxis.setTextColor(Color.parseColor("#FFFFFFFF"));
        rightAxis.setAxisLineColor(Color.parseColor("#FFFFFFFF"));

        ValueFormatter yAxisLeftFormatter = new IndexAxisValueFormatter(new ArrayList<>());
        rightAxis.setValueFormatter(yAxisLeftFormatter);
        YAxis leftAxis = scatterChart.getAxisLeft();
        leftAxis.setTextColor(Color.parseColor("#FFFFFFFF"));
        leftAxis.setAxisLineColor(Color.parseColor("#FFFFFFFF"));
        leftAxis.setTextSize(14);

        Legend legend = scatterChart.getLegend();
        legend.setFormSize(1);
        legend.setForm(Legend.LegendForm.NONE);

        scatterChart.setTouchEnabled(true);
        scatterChart.setPinchZoom(false);
        scatterChart.setDoubleTapToZoomEnabled(true);


        ScatterDataSet scatterDataSet = new ScatterDataSet(scatterEntries, "hehe");
        scatterDataSet.setScatterShapeSize(50f);
        scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        scatterDataSet.setDrawValues(false);
        ScatterData scatterData = new ScatterData(scatterDataSet);
        scatterChart.setData(scatterData);
        scatterData.setValueTextColor(Color.parseColor("#FFFFFFFF"));
        scatterDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        scatterDataSet.setValueTextColor(Color.parseColor("#FFFFFFFF"));
        scatterDataSet.setValueTextSize(13f);

        scatterChart.invalidate();
        scatterChart.refreshDrawableState();
    }
}
