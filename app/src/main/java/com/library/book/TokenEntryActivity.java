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

import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TokenEntryActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_entry);
        setTitle("Verify OTP");

        email = getIntent().getStringExtra("email");
    }

    String email;


    public void btn_tokenverify(View view) {

        EditText otp = (EditText) findViewById(R.id.otp);

        if (otp.getText().toString().trim().equals(""))
            otp.setError("This field is required");

        else
            new TokenVefication(otp.getText().toString().trim()).execute(null, null, null);


    }

    private class TokenVefication extends AsyncTask<Void, Void, Void> {


        private String token;
        private ProgressDialog progressDialog;
        JSONObject response;

        public TokenVefication(String token) {
            this.token = token;
        }

        @Override
        protected Void doInBackground(Void... params) {


            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();

                map.put("email", email);
                map.put("token", token);

                response = remoteConnection.getJSON(Util.maptoString(map), "TokenVerificationServlet").getJSONObject(0);


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
                    if (response.getString("invalid").equals("yes")) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                TokenEntryActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("Invalid token");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Invalid token or this token is expired!")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                        ;

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                    } else {

                        new AlertDialog.Builder(TokenEntryActivity.this)
                                .setTitle("Registration Successful")
                                .setMessage("Click ok to login !")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete

                                        Intent intent = new Intent(TokenEntryActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                })

                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(TokenEntryActivity.this, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
