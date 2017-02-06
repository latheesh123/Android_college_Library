package com.library.book;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.library.adapter.BooksAdapter;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchBooksActivity extends AppCompatActivity {

    private BooksAdapter adapter;
    ArrayList<Book> bookslist = new ArrayList<Book>();
    ArrayList<Book> focusbookslist = new ArrayList<Book>();
    final ArrayList<String> searchby = new ArrayList();
    AlertDialog.Builder builder;


    public void filterbtn(View view) {
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Search");


        setContentView(R.layout.activity_search_books);
        ListView listView1 = (ListView) findViewById(R.id.listView1);

        final EditText editsearch = (EditText) findViewById(R.id.search);

        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString().toLowerCase();
                adapter.filter(text,searchby);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        adapter =
                new BooksAdapter(this, 0, focusbookslist, 0);
        listView1.setAdapter(adapter);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SearchBooksActivity.this);
        builderSingle.setTitle("Search Filter");
        final String[] items = {"Title", "Author", "Year of publishing"};
        boolean[] checkedValues = new boolean[items.length];
        checkedValues[0] = true;
        checkedValues[1] = true;
        checkedValues[2] = true;


        List<String> temp = Arrays.asList(items);
        searchby.addAll(temp);

        builder =
                new AlertDialog.Builder(SearchBooksActivity.this);

        builder.setTitle("Search By").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        })
                .setMultiChoiceItems(items, checkedValues,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked) {
                                    searchby.add(items[item]);
                                } else {
                                    if (searchby.contains(items[item]))
                                        searchby.remove(items[item]);
                                }
                            }

                        });


        builder.create();

    }

    @Override
    public void onResume() {
        super.onResume();

        new GetBooksTest().execute(null, null, null);

    }


    private class GetBooksTest extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                bookslist.clear();
                focusbookslist.clear();

                Map<String, String> map = new HashMap<>();
                response = remoteConnection.getJSON(Util.maptoString(map), "GetBooksServlet");
                for (int i = 0; i < response.length(); i++) {

                    Gson gson = new Gson();
                    JSONObject jsonObject = response.getJSONObject(i);
                    Book book = gson.fromJson(jsonObject.toString(), Book.class);
                    bookslist.add(book);
                    focusbookslist.add(book);

                }

                adapter.filterdata.addAll(bookslist);

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

            progressDialog = ProgressDialog.show(SearchBooksActivity.this, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
