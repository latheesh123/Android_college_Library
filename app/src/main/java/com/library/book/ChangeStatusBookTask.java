package com.library.book;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        if(response!=null){

            try {
                JSONObject jsonObject = response.getJSONObject(0);
                if(jsonObject.has("issued")){

                    if(jsonObject.getString("issued").equals("false")){
                        issuedbook = false;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(issuedbook) {
            Toast.makeText(activity, "Status Changed", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(activity, "You can not reserve more than 3 books", Toast.LENGTH_LONG).show();

        }
        activity.finish();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(activity, "Please wait ...", "", true);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}