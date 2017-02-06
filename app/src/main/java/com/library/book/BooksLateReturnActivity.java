package com.library.book;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BooksLateReturnActivity extends ChartActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView charttype = (TextView) findViewById(R.id.charttype);
        charttype.setText("Number of late returns Vs on time returns");

        new ChartTask(this, "BooksLateReturnServlet").execute(null, null, null);

    }


}
