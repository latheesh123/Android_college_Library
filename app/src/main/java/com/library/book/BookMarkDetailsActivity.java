package com.library.book;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.library.adapter.BookMarkAdapter;
import com.library.adapter.BooksAdapter;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;
import com.library.pojo.BookMarks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookMarkDetailsActivity extends ParentActivity {
    private BookMarkAdapter adapter;
    ArrayList<BookMarks> bookMarks = new ArrayList<BookMarks>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark_details);
        ListView listView1 = (ListView) findViewById(R.id.listView1);

        setTitle("Book marks");
        adapter =
                new BookMarkAdapter(this, 0, bookMarks);
        listView1.setAdapter(adapter);
        int bookid = getIntent().getIntExtra("bookid", 0);
        new GetBookMarksTask("" + bookid, "" + sharedpreferences.getString(Util.userid, "")).execute(null, null, null);


    }


    public String getLocationName(String latitude , String longitude){

        String address ="";
        address = "Latitude "+latitude+" Longitude"+longitude;

        try {

            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
            if (addresses.size() > 0) {
                address += addresses.get(0).getAddressLine(0)+" , "+addresses.get(0).getAddressLine(1)+" "+addresses.get(0).getAddressLine(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return address;
        }
        return  address;
    }

    private class GetBookMarksTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;
        String userid;
        String bookid;

        public GetBookMarksTask(String userid,String bookid) {
            this.userid = userid;
            this.bookid = bookid;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("userid", userid);
                map.put("bookid", bookid);

                response = remoteConnection.getJSON(Util.maptoString(map), "GetBookMarksServlet");
                bookMarks.clear();

                for (int i = 0; i < response.length(); i++) {

                    Gson gson = new Gson();
                    JSONObject jsonObject = response.getJSONObject(i);
                    BookMarks bookmark = gson.fromJson(jsonObject.toString(), BookMarks.class);
                    bookmark.setLocationname(getLocationName(bookmark.getLatitude(),bookmark.getLongitude()));
                    bookMarks.add(bookmark);

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

            progressDialog = ProgressDialog.show(BookMarkDetailsActivity.this, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
