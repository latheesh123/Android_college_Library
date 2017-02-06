package com.library.book;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.library.bean.SessionHandling;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Registration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("Change Password");
    }

    public void changepassword(View view) {

        EditText oldpassword = (EditText) findViewById(R.id.oldpassword);
        EditText newpassword = (EditText) findViewById(R.id.newpassword);
        EditText confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        if (oldpassword.getText().toString().trim().equals(""))
            oldpassword.setError("This field is required");

        else if (newpassword.getText().toString().trim().equals(""))
            newpassword.setError("This field is required");
        else if (confirmpassword.getText().toString().trim().equals(""))
            confirmpassword.setError("This field is required");

        else if (!confirmpassword.getText().toString().equals(newpassword.getText().toString()))
            newpassword.setError("Passwords does not match");

        else
           new ChangePasswordTask(sharedpreferences.getString(Util.userid,""),oldpassword.getText().toString().trim(),newpassword.getText().toString().trim(),this).execute(null,null,null);
    }


    private class ChangePasswordTask extends AsyncTask<Void, Void, Void> {


        private String userid;
        private String oldpassword;
        private Activity activity;
        private String newpassword;
        private ProgressDialog progressDialog;
        JSONObject response;

        public ChangePasswordTask(String userid, String oldpassword, String newpassword, Activity activity) {
            this.userid = userid;
            this.oldpassword = oldpassword;
            this.newpassword = newpassword;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("userid", userid);
                map.put("oldpassword", oldpassword);
                map.put("newpassword", newpassword);
                response = remoteConnection.getJSON(Util.maptoString(map), "ChangePasswordServlet").getJSONObject(0);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();


            if (response == null) {

            }
            if (response != null) {

                try {
                    if (response.getString("result").equals("false")) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                ChangePasswordActivity.this);
                        alertDialogBuilder.setTitle("Invalid current password");
                        alertDialogBuilder
                                .setMessage("Please enter valid current password!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                        ;
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else {


                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                ChangePasswordActivity.this);
                        alertDialogBuilder.setTitle("Password changed");
                        alertDialogBuilder
                                .setMessage("Password changed successfully!")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                        ;
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
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

}
