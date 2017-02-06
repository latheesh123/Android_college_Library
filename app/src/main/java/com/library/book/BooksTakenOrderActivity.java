package com.library.book;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.library.adapter.BooksTakenAdapter;
import com.library.bean.SessionHandling;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;
import com.library.pojo.BooksTaken;
import com.library.pojo.Registration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BooksTakenOrderActivity extends ParentActivity implements View.OnClickListener {

    private ListView listView;
    BooksTakenAdapter booksTakenAdapter;
    ArrayList<BooksTaken> data = new ArrayList<>();

    private EditText from_date, to_Time;
    private SimpleDateFormat dateFormatter;

    private DatePickerDialog frompicker;
    private DatePickerDialog topicker;


    private long fromdate;
    private long enddate;

    @Override
    public void onClick(View view) {

        if (view == from_date) {
            frompicker.show();
        } else if (view == to_Time) {
            topicker.show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_taken_order);
        listView = (ListView) findViewById(R.id.reportsdetail);
        booksTakenAdapter = new BooksTakenAdapter(this, 0, data);
        listView.setAdapter(booksTakenAdapter);
setTitle("Books taken on days");
        from_date = (EditText) findViewById(R.id.from_date);
        to_Time = (EditText) findViewById(R.id.to_Time);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        init();

        //new BooksTakenOrderTask(this).execute(null, null, null);

    }

    public void buildchart(View view) {

        Map<String, String> map = new HashMap<>();
        map.put("fromtime", "" + fromdate);
        map.put("totime", "" + enddate);


        if (fromdate >= enddate) {

            from_date.setError("Invalid date range");
        } else {
            from_date.setError(null);
            new BooksTakenOrderTask(this, "" + fromdate, "" + enddate).execute(null, null, null);

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


    private class BooksTakenOrderTask extends AsyncTask<Void, Void, Void> {


        private Activity activity;
        private ProgressDialog progressDialog;
        JSONArray response;
        String startdate;
        String enddate;

        public BooksTakenOrderTask(Activity activity, String startdate, String enddate) {
            this.activity = activity;
            this.startdate = startdate;
            this.enddate = enddate;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("startdate", startdate);
                map.put("enddate", enddate);

                response = remoteConnection.getJSON(Util.maptoString(map), "BooksTakenOrderServlet");

                data.clear();

                for (int i = 0; i < response.length(); i++) {

                    Gson gson = new Gson();
                    JSONObject jsonObject = response.getJSONObject(i);
                    BooksTaken book = gson.fromJson(jsonObject.toString(), BooksTaken.class);

                    data.add(book);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();

            System.out.println("Books size" + data.size());
            booksTakenAdapter.notifyDataSetChanged();
            ;

            if (response == null) {

            }

            if (response != null) {

            }
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(activity, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
