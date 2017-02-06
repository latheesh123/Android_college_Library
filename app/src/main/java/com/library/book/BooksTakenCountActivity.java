package com.library.book;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BooksTakenCountActivity extends ChartActivity implements View.OnClickListener {


    private EditText from_date, to_Time;
    private SimpleDateFormat dateFormatter;

    private DatePickerDialog frompicker;
    private DatePickerDialog topicker;


    private long fromdate;
    private long enddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView charttype = (TextView) findViewById(R.id.charttype);
        charttype.setText("Books Issues Vs Book Returns");
setTitle("Books Issues Vs Book Returns");
        LinearLayout dateranges = (LinearLayout) findViewById(R.id.dateranges);
        dateranges.setVisibility(View.VISIBLE);

        from_date = (EditText) findViewById(R.id.from_date);
        to_Time = (EditText) findViewById(R.id.to_Time);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        init();


    }

    public void buildchart(View view) {

        Map<String, String> map = new HashMap<>();
        map.put("fromtime", "" + fromdate);
        map.put("totime", "" + enddate);


        if (fromdate >= enddate) {

            from_date.setError("Invalid date range");
        } else {
            from_date.setError(null);
          ChartTask task =    new ChartTask(this, "BooksTakenServlet");
            task.prametersmap = map;
            task.execute(null, null, null);
        }
    }

    @Override
    public void onClick(View view) {

        if (view == from_date) {
            frompicker.show();
        } else if (view == to_Time) {
            topicker.show();
        }

    }

    private void init() {
        from_date.setOnClickListener(this);
        to_Time.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        frompicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(year, monthOfYear, dayOfMonth);

                fromdate = calendar.getTimeInMillis() / 1000L;


                from_date.setText(dateFormatter.format(calendar.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        topicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(year, monthOfYear, dayOfMonth);
                to_Time.setText(dateFormatter.format(calendar.getTime()));
                enddate = calendar.getTimeInMillis() / 1000L;

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


}
