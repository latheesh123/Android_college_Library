package com.library.book;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReturnConfirmationTask extends AsyncTask<Void, Void, Void> {


    private ProgressDialog progressDialog;
    JSONArray response;
    String barcodeid;
    Activity activity;

    public ReturnConfirmationTask(String barcodeid, Activity activity) {
        this.barcodeid = barcodeid;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {

        RemoteConnection remoteConnection = new RemoteConnection();
        try {

            Map<String, String> map = new HashMap<>();
            map.put("barcodeid", barcodeid);
            response = remoteConnection.getJSON(Util.maptoString(map), "ReturnBookConfirmationServlet");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        progressDialog.dismiss();
        if (response == null || response.length()==0) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            builder1.setTitle("Wrong Entry");
            builder1.setMessage("This book is not issued or invalid book id");
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

        } else {

            try {
                JSONObject jsonObject = response.getJSONObject(0);
                String studentid = jsonObject.getString("studentid");
                final String userid = jsonObject.getString("userid");
                final String username = jsonObject.getString("username");
                final String id = jsonObject.getString("bookid");

                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setTitle("Confirm return");
                builder1.setMessage("You want to return book from the user ? \n\n Student id  : " + studentid + " Name : " + username);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int clickid) {
                                new ChangeStatusBookTask("" + id, "" + userid, "" + Util.RETURNED, activity).execute(null, null, null);

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
