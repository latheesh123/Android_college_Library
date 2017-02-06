package com.library.book.rest;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteConnection {


private static final String URL =
           "http://ec2-54-200-152-184.us-west-2.compute.amazonaws.com:8080/librarymanagement/";


    static String HTTP_METHOD_POST = "POST";
    static String HTTP_REQUEST_PROPERTY_CONTENT_TYPE = "Context-Type";
    static String HTTP_REQUEST_PROPERTY_CONTENT_TYPE_VALUE_URL_ENCODED = "application/x-www-form-urlencoded";


    public JSONArray getJSON(String keyvaluepair, String controller) {
        try {

            URL url;

            url = new URL(URL + controller);


            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(HTTP_METHOD_POST);
            connection
                    .setRequestProperty(
                            HTTP_REQUEST_PROPERTY_CONTENT_TYPE,
                            HTTP_REQUEST_PROPERTY_CONTENT_TYPE_VALUE_URL_ENCODED);

            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setInstanceFollowRedirects(false);


            DataOutputStream requestData = new DataOutputStream(
                    connection.getOutputStream());
            requestData.writeBytes(keyvaluepair);

            requestData.flush();
            requestData.close();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer();
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            System.out.println(json.toString());
            JSONArray data = new JSONArray(json.toString());

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
