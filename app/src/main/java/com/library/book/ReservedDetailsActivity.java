package com.library.book;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.library.adapter.BooksAdapter;
import com.library.adapter.ReservedUsersAdapter;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;
import com.library.pojo.UserReservation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReservedDetailsActivity extends AppCompatActivity {

    private ReservedUsersAdapter adapter;


    ArrayList<UserReservation> reservations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_details);
        final String bookid = getIntent().getStringExtra("bookid");

        setTitle("Reserved by");
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        adapter =
                new ReservedUsersAdapter(this, 0, reservations);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                final UserReservation userReservation = reservations.get(position);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ReservedDetailsActivity.this);

                alertDialogBuilder.setTitle("Issue Book");

                alertDialogBuilder
                        .setMessage("Issue Book to !" + userReservation.getStudentid())
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                userReservation.setStatus(Util.ISSUED);
                                adapter.notifyDataSetChanged();;
                                new IssueBookTask(userReservation.getBookid(), "" + userReservation.getUserid(), "" + Util.ISSUED, ReservedDetailsActivity.this).execute(null, null, null);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        new GetBookDetailsTask(this, bookid).execute(null, null, null);


    }


    private class IssueBookTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;
        String bookid;
        String userid;
        String status;
        Activity activity;

        public IssueBookTask(String bookid, String userid, String status, Activity activity) {
            this.bookid = bookid;
            this.userid = userid;
            this.status = status;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("bookid", bookid);
                map.put("userid", userid);
                map.put("status", status);

                response = remoteConnection.getJSON(Util.maptoString(map), "IssueBookServlet");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

            Toast.makeText(activity, "Status Changed", Toast.LENGTH_LONG).show();

            adapter.notifyDataSetChanged();;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(activity, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetBookDetailsTask extends AsyncTask<Void, Void, Void> {


        private Activity activity;
        private ProgressDialog progressDialog;
        JSONArray response;
        String bookid;

        public GetBookDetailsTask(Activity activity, String bookid) {
            this.activity = activity;
            this.bookid = bookid;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("bookid", bookid);

                response = remoteConnection.getJSON(Util.maptoString(map), "ReservedDetailsServlet");
                for (int i = 0; i < response.length(); i++) {

                    Gson gson = new Gson();
                    JSONObject jsonObject = response.getJSONObject(i);
                    UserReservation userReservation = gson.fromJson(jsonObject.toString(), UserReservation.class);
                    reservations.add(userReservation);
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

            progressDialog = ProgressDialog.show(activity, "Please wait .", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
