package com.library.book;


import android.os.Bundle;
import android.widget.TextView;

public class BooksCountChartActivity extends ChartActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView charttype = (TextView) findViewById(R.id.charttype);
        charttype.setText("Total Number of Books vs Number of reserverd book");

        new ChartTask(this,"BooksCountServlet").execute(null,null,null);

    }



}
