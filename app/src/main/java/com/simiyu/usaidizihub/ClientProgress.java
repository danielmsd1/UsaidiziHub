package com.simiyu.usaidizihub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ClientProgress extends AppCompatActivity {


    /**
     * IDEA: Get daily mood updates and use the data
     * to design the chart.
     */
    float mood[] ={23,23,21,53,64,75,30};
    String day[]={"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_progress);
        setPieChart();
    }
    private void setPieChart(){
        //populating a list of pieEntries
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0;i<mood.length;i++){
            pieEntries.add(new PieEntry(mood[i],day[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"Days of the week");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart chart = findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }
}