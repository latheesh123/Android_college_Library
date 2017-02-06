package com.library.book;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.library.adapter.BooksAdapter;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBooksActivity extends AppCompatActivity {

    private BooksAdapter adapter;
    ArrayList<Book> bookslist = new ArrayList<Book>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        setTitle("My Books");

        ListView listView1 = (ListView) findViewById(R.id.listView1);

        adapter =
                new BooksAdapter(this, 0, bookslist, 1);
        adapter.setMybooks(true);
        listView1.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onResume();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("My books resume");
        SharedPreferences sharedpreferences = getSharedPreferences(
                Util.preference, Context.MODE_PRIVATE);

        new GetBooksTest(sharedpreferences.getString(Util.userid, "")).execute(null, null, null);

    }

    private class GetBooksTest extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;
        String userid;

        public GetBooksTest(String userid) {
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("userid", userid);
                response = remoteConnection.getJSON(Util.maptoString(map), "MyBooksServlet");
                bookslist.clear();

                for (int i = 0; i < response.length(); i++) {

                    Gson gson = new Gson();
                    JSONObject jsonObject = response.getJSONObject(i);
                    Book book = gson.fromJson(jsonObject.toString(), Book.class);
                    bookslist.add(book);

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

            progressDialog = ProgressDialog.show(MyBooksActivity.this, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
