package com.library.book;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ColorFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends ParentActivity {

    PieChart mChart;

    public static final int[] MY_COLORS = {
            Color.rgb(217,83,79), Color.rgb(91,192,222), Color.rgb(92,184,92),
            Color.rgb(66,139,202), Color.rgb(217,83,79)
    };

    public int GCD(int a, int b) {
        if (b==0) return a;
        return GCD(b,a%b);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_vs_librarians);


        // setting sample Data for Pie Chart
        //setDataForPieChart();
    }

    public void setDataForPieChart(ArrayList<Integer> yValues, ArrayList<String> xValues) {

        mChart = (PieChart) findViewById(R.id.chart1);

       // mChart.s

//        mChart.setRotationEnabled(true);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {


            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;

                Toast.makeText(ChartActivity.this,
                        e.getX() + " is " + e.getY() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        List<PieEntry> yVals1 = new ArrayList<PieEntry>();

        for (int i = 0; i < yValues.size(); i++)
            yVals1.add(new PieEntry(yValues.get(i), xValues.get(i)));

        List<String> xVals = new ArrayList<String>();


        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "Count");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new DefaultValueFormatter(0));
     //   data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateXY(1400, 1400);
        Description description =  new Description();
        description.setText("");
        mChart.setDescription(description);



        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);


        TextView ratio = (TextView)findViewById(R.id.ratio);

        System.out.println("Ratio"+yValues.size());
        if(yValues.size()>=2) {


            int gcd = GCD(yValues.get(0), yValues.get(1));
            System.out.println("GCD" + gcd);
            if(gcd !=0) {
                ratio.setText("RATIO : " + (yValues.get(0) / gcd) + " : " + (yValues.get(1) / gcd));
            }
        }
    }

    public class MyValueFormatter implements IValueFormatter {


        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return String.valueOf((int)value);
        }
    }

}
