package com.library.book;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddSingleBookThread extends AsyncTask<Void, Void, Void> {

    private ProgressDialog progressDialog;
    JSONArray response;
    String barcodeid;
    Activity activity;

    public AddSingleBookThread(String barcodeid, Activity activity) {
        this.barcodeid = barcodeid;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {

        RemoteConnection remoteConnection = new RemoteConnection();
        try {

            Map<String, String> map = new HashMap<>();
            map.put("bookid", barcodeid);
            response = remoteConnection.getJSON(Util.maptoString(map), "AddSingleBookServlet");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        progressDialog.dismiss();


        try {
            JSONObject jsonObject = response.getJSONObject(0);
            Book book = new Book();



            if(jsonObject.has("volumeInfo")) {
                JSONObject volumeinfo = jsonObject
                        .getJSONObject("volumeInfo");

                if (volumeinfo.has("description")) {
                    book.setDescription(volumeinfo.getString("description"));
                }
                if (volumeinfo.has("title")) {
                    book.setTitle(volumeinfo.getString("title"));
                }
                if (volumeinfo.has("subtitle")) {
                    book.setSubtitle(volumeinfo.getString("subtitle"));
                }
                if (volumeinfo.has("authors")) {
                    book.setAuthors(volumeinfo.getString("authors"));
                }
                if (volumeinfo.has("publisher")) {
                    book.setPublisher(volumeinfo.getString("publisher"));
                }
                if (volumeinfo.has("publishedDate")) {
                    book.setPublishedDate(volumeinfo.getString("publishedDate"));
                }
            }


            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);

            if(book.getTitle()!=null) {

                builder1.setTitle("Book Added");
                builder1.setMessage("Title : " + book.getTitle() + "\n" + "Sub Title : " + book.getSubtitle() + "\n" + "Authors : " + book.getAuthors() + "\n" + "Description : " + book.getDescription() + "\n" + "Publisher : " + book.getPublisher() + "\n" + "Published Date : " + book.getPublishedDate());
            }
            else{

                builder1.setTitle("Book can not be added");
                builder1.setMessage("Book already added/ error adding book");

            }
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            activity.finish();
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();


        } catch (JSONException e) {
            e.printStackTrace();
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
