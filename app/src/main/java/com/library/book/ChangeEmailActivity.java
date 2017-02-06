package com.library.book;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeEmailActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        setTitle("Change email");
    }

    public void changeemail(View view) {

        EditText newemail = (EditText) findViewById(R.id.newemail);
        if (newemail.getText().toString().trim().equals(""))
            newemail.setError("This field is required");

        else
            new ChangeEmailTask(sharedpreferences.getString(Util.userid, ""), newemail.getText().toString().trim(), this).execute(null,null,null);


    }


    private class ChangeEmailTask extends AsyncTask<Void, Void, Void> {


        private String userid;
        private String newemail;
        private Activity activity;
        private ProgressDialog progressDialog;
        JSONObject response;

        public ChangeEmailTask(String userid, String newemail, Activity activity) {
            this.userid = userid;
            this.newemail = newemail;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("userid", userid);
                map.put("newemail", newemail);
                response = remoteConnection.getJSON(Util.maptoString(map), "ChangeEmailServlet").getJSONObject(0);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    ChangeEmailActivity.this);
            alertDialogBuilder.setTitle("Email changed");
            alertDialogBuilder
                    .setMessage("Email changed successfully!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
            ;
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


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
