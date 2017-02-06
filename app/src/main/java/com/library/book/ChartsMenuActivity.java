package com.library.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChartsMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_menu);
        setTitle("Statistics");
        ;
    }

    public void studentchart(View view) {

        Intent intent = new Intent(this, StudentsVsLibrarians.class);
        startActivity(intent);
    }

    public void remainingbookschart(View view) {

        Intent intent = new Intent(this, BooksCountChartActivity.class);
        startActivity(intent);
    }


    public void bookstakengraph(View view) {

        Intent intent = new Intent(this, BooksTakenCountActivity.class);
        startActivity(intent);
    }

    public void returnsgraph(View view) {

        Intent intent = new Intent(this, BooksLateReturnActivity.class);
        startActivity(intent);
    }

    public void morebookstakengraph(View view) {

        Intent intent = new Intent(this, BooksTakenOrderActivity.class);
        startActivity(intent);
    }
}
