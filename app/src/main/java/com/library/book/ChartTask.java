package com.library.book;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChartTask extends AsyncTask<Void, Void, Void> {


    private ProgressDialog progressDialog;
    JSONArray response;

    public Map<String, String> prametersmap = null;




    ArrayList<String> xValues = new ArrayList<>();
    ArrayList<Integer> yvalues = new ArrayList<>();

    ChartActivity chartActivity;
    String servlet;

    public ChartTask(ChartActivity chartActivity,String servlet) {

        this.chartActivity = chartActivity;
        this.servlet = servlet;
    }

    @Override
    protected Void doInBackground(Void... params) {

        RemoteConnection remoteConnection = new RemoteConnection();
        try {

            Map<String, String> map = new HashMap<>();

            if(prametersmap !=null){

                map = prametersmap;

            }
            response = remoteConnection.getJSON(Util.maptoString(map), servlet);

            for (int i = 0; i < response.length(); i++) {

                Gson gson = new Gson();
                JSONObject jsonObject = response.getJSONObject(i);
                Iterator<?> keys = jsonObject.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    xValues.add(key);
                    yvalues.add(jsonObject.getInt(key));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void result) {

        progressDialog.dismiss();
        chartActivity.setDataForPieChart(yvalues, xValues);
    }

    @Override
    protected void onPreExecute() {

        progressDialog = ProgressDialog.show(chartActivity, "Please wait ...", "", true);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}

