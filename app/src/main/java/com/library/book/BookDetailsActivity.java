package com.library.book;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookDetailsActivity extends ParentActivity {


    private String RESERVE = "Reserve";
    private String CANCEL = "Cancel";
    private String DETAILS = "Booking Details";

    private String barcodeid = "";
    int bookid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Book Details");
        setContentView(R.layout.activity_book_details);

        if (getIntent().hasExtra("strbookid")) {
            bookid = Integer.parseInt(getIntent().getStringExtra("strbookid"));
        } else {
            bookid = getIntent().getIntExtra("bookid", 0);
        }

        barcodeid = getIntent().getStringExtra("barcodeid");
    }

    protected void onResume() {
        super.onResume();
        ;
        new GetBookDetailsTask("" + bookid, "" + sharedpreferences.getString(Util.userid, "")).execute(null, null, null);

    }

    String title = "";

    public void setDataToUI(Book book) {

        ImageView image = (ImageView) findViewById(R.id.image);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(this).load(book.getSmallThumbnail()).into(image);

        TextView param1 = (TextView) findViewById(R.id.param1);
        param1.setText(Html.fromHtml("<b>Title : </b>" + book.getTitle()));

        title = book.getTitle();

        TextView param2 = (TextView) findViewById(R.id.param2);
        param2.setText(Html.fromHtml("<b>Sub Title : </b>" + book.getSubtitle()));

        TextView param3 = (TextView) findViewById(R.id.param3);
        param3.setText(Html.fromHtml("<b>Author : </b>" + book.getAuthors()));

        TextView param4 = (TextView) findViewById(R.id.param4);
        param4.setText(Html.fromHtml("<b>Publisher : </b>" + book.getPublisher()));

        TextView param5 = (TextView) findViewById(R.id.param5);
        param5.setText(Html.fromHtml("<b>Published Date : </b>" + book.getPublishedDate()));

        TextView param6 = (TextView) findViewById(R.id.param6);
        param6.setText(Html.fromHtml("<b>Description : </b>" + book.getDescription()));

        Button reserveBook = (Button) findViewById(R.id.reserveBook);

        if (book.getStatus() == Util.NOSTATUS || book.getStatus() == Util.RETURNED) {

            reserveBook.setText(RESERVE);
        } else if (book.getStatus() == Util.RESERVED) {

            reserveBook.setText(CANCEL);
        }

        if (sharedpreferences.getString(Util.type, "").equals("1")) {
            reserveBook.setText(DETAILS);
        }

    }

    public void reserverBook(View view) {

        Button button = (Button) view;
        int bookid = getIntent().getIntExtra("bookid", 0);

        if (button.getText().equals(RESERVE)) {

            new ChangeStatusBookTask("" + bookid, "" + sharedpreferences.getString(Util.userid, ""), "" + Util.RESERVED, this).execute(null, null, null);

        }

        if (button.getText().equals(CANCEL)) {
            new ChangeStatusBookTask("" + bookid, "" + sharedpreferences.getString(Util.userid, ""), "" + Util.CANCELLED, this).execute(null, null, null);

        }
        if (button.getText().equals(DETAILS)) {

            Intent intent = new Intent(this, ReservedDetailsActivity.class);
            intent.putExtra("bookid", barcodeid);
            startActivity(intent);
        }
    }

    public void searchBook(View view) {

        Intent intent = new Intent(this, GoogleResultsActivity.class);
        System.out.println("http://www.google.com/search?q=" + title.replaceAll(" ", "+"));
        intent.putExtra("url", "http://www.google.com/search?q=" + title.replaceAll(" ", "+"));
        startActivity(intent);
    }


    private class GetBookDetailsTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;
        String bookid;
        String userid;
        Book book = new Book();

        public GetBookDetailsTask(String bookid, String userid) {
            this.bookid = bookid;
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("bookid", bookid);
                map.put("userid", userid);
                response = remoteConnection.getJSON(Util.maptoString(map), "GetParticularBookServlet");
                for (int i = 0; i < response.length(); i++) {

                    Gson gson = new Gson();

                    JSONObject jsonObject = response.getJSONObject(i);
                    book = gson.fromJson(jsonObject.toString(), Book.class);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            setDataToUI(book);
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(BookDetailsActivity.this, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public class ChangeStatusBookTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;
        String bookid;
        String userid;
        String status;
        Activity activity;

        public ChangeStatusBookTask(String bookid, String userid, String status, Activity activity) {
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

                response = remoteConnection.getJSON(Util.maptoString(map), "ChangeStatusServlet");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

            boolean issuedbook = true;
            System.out.println(response);
            if (response != null) {

                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    if (jsonObject.has("issued")) {

                        if (jsonObject.getString("issued").equals("false")) {
                            issuedbook = false;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (issuedbook) {
                Toast.makeText(activity, "Status Changed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "You can not reserve more than 3 books", Toast.LENGTH_LONG).show();

            }
            Intent intent = getIntent();
            intent.putExtra("bookid", getIntent().getIntExtra("bookid", 0));
            finish();
            startActivity(intent);
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
