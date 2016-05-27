package com.example.woqja.viewpager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    LineChart lineChart;
    LineData data;
    private ArrayList<String> labels;

    private ArrayList<Entry> entries;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_linechart, container, false);

        // creating list of entry

        lineChart = (LineChart) view.findViewById(R.id.chart);
        entries = new ArrayList<Entry>();
        labels = new ArrayList<String>();
           entries.add(new Entry(4f, 0));
            entries.add(new Entry(8f, 1));
            entries.add(new Entry(6f, 2));
            entries.add(new Entry(2f, 3));
            entries.add(new Entry(18f, 4));
            entries.add(new Entry(9f, 5));

        LineDataSet dataset = new LineDataSet(entries, "");

        dataset.setDrawCircles(true);
        dataset.setDrawValues(true);
        dataset.setCircleColor(Color.parseColor("#454587"));
        dataset.setColor(Color.parseColor("#EFEADF"));
        dataset.setCircleSize(5);
        dataset.setLineWidth(4);
        //dataset.setDrawFilled(true);


     labels.add("January");
            labels.add("February");
            labels.add("March");
            labels.add("April");
            labels.add("May");
            labels.add("June");


        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        data = new LineData(labels,dataset);
        lineChart.setTouchEnabled(false);
        lineChart.setDrawGridBackground(true);

        //세로 제거
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawLabels(true);
        lineChart.getXAxis().setLabelsToSkip(0);
        lineChart.getXAxis().setDrawAxisLine(false);

        //가로 제거
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisLeft().setGridColor(Color.parseColor("#EFEADF"));
        lineChart.getAxisLeft().setStartAtZero(false);
        lineChart.getAxisRight().setStartAtZero(false);
        //lineChart.setVisibleYRangeMaximum(110, YAxis.AxisDependency.LEFT);
        lineChart.setDescription("");
        lineChart.setGridBackgroundColor(Color.WHITE);
        lineChart.setData(data);


        return view;
    }

}
