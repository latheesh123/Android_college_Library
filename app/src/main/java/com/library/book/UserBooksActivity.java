package com.library.book;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.library.adapter.UserBooksAdapter;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.ListByDate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserBooksActivity extends AppCompatActivity {

    ArrayList<ListByDate> data = new ArrayList<>();
    private UserBooksAdapter adapter;

    private EditText studentnameet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        setTitle("Search User");
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        adapter = new UserBooksAdapter(this, 0, data);
        listView1.setAdapter(adapter);

        studentnameet = (EditText) findViewById(R.id.studentname);
      //  new ListByDateTask("" + studentnameet.getText().toString()).execute(null, null, null);
    }

    public void getData(View view){
        studentnameet = (EditText) findViewById(R.id.studentname);
        if(studentnameet.getText().toString().trim().equals("")){
            studentnameet.setError("Enter student name");
        }
        else {
            studentnameet.setError(null);

            new ListByDateTask("" + studentnameet.getText().toString()).execute(null, null, null);
        }
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
                map.put("name", time);
                data.clear();
                response = remoteConnection.getJSON(Util.maptoString(map), "UserBooksServlet");
                System.err.println("Response length" + response.length());
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

            progressDialog = ProgressDialog.show(UserBooksActivity.this, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
