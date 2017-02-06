package com.library.book;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.library.adapter.ListByDateAdapter;
import com.library.adapter.ReservedUsersAdapter;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;
import com.library.pojo.ListByDate;
import com.library.pojo.UserReservation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ListByDateActivity extends AppCompatActivity implements View.OnClickListener {

    private DatePickerDialog datePickerDialog;

    private EditText dateet;

    ArrayList<ListByDate> data = new ArrayList<>();
    private ListByDateAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_by_date);
        setTitle("List By date");
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        adapter = new ListByDateAdapter(this, 0, data);
        listView1.setAdapter(adapter);

        dateet = (EditText) findViewById(R.id.date);
        initTimePicker();
        getData(null);
    }

    @Override
    public void onClick(View view) {

        if (view == dateet) {
            datePickerDialog.show();
        }
    }



    public void getData(View view){

        String time =dateet.getText().toString();

        Date date = null;
        try {
            date = Util.format1.parse(time );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTime = (long) date.getTime()/1000;

        adapter.choosentime = date.getTime();

        new ListByDateTask(""+unixTime).execute(null,null,null);
    }

    private void initTimePicker() {
        dateet.setText(Util.format1.format(new Date()));
        dateet.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateet.setText(Util.format1.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private class ListByDateTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;
        String time;

        public ListByDateTask(String time) {
            this.time = time;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("time", time);
                data.clear();
                response = remoteConnection.getJSON(Util.maptoString(map), "ListByDateServlet");
                System.err.println("Response length"+response.length());
                for (int i = 0; i < response.length(); i++) {

                    Gson gson = new Gson();
                    JSONObject jsonObject = response.getJSONObject(i);
                    ListByDate listByDate = gson.fromJson(jsonObject.toString(), ListByDate.class);
                    data.add(listByDate);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(ListByDateActivity.this, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
