package com.library.book;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {
    public SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(
                Util.preference, Context.MODE_PRIVATE);


        setContentView(R.layout.activity_login);
        setTitle("Login");
    }

    protected void onResume() {
        super.onResume();
        if (sharedpreferences.contains(Util.userid)) {

            Intent intent;

            if (sharedpreferences.getString(Util.type, "").equals("1"))
                intent = new Intent(this, AdminLandingPageActivity.class);
            else
                intent = new Intent(this, UserLandingPageActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }
    }


    public void btn_adminReg(View view) {

        Intent intent = new Intent(this, AdminRegisterActivity.class);
        startActivity(intent);

    }


    public void btn_userReg(View view) {

        Intent intent = new Intent(this, UserRegistrationActivity.class);
        startActivity(intent);

    }

    public void btn_Login(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        if (username.getText().toString().trim().equals(""))
            username.setError("This field is required");

        else if (password.getText().toString().trim().equals(""))
            password.setError("This field is required");

        else
            new LoginTask(username.getText().toString().trim(), password.getText().toString().trim(), this).execute(null, null, null);

    }

    private class LoginTask extends AsyncTask<Void, Void, Void> {


        private String password;
        private String username;
        private Activity activity;
        private ProgressDialog progressDialog;
        JSONObject response;

        public LoginTask(String username, String password, Activity activity) {
            this.password = password;
            this.username = username;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);

                response = remoteConnection.getJSON(Util.maptoString(map), "LoginServlet").getJSONObject(0);


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
                    if (response.getString("valid").equals("no")) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                LoginActivity.this);
                        alertDialogBuilder.setTitle("Invalid credentials");
                        alertDialogBuilder
                                .setMessage("Please enter valid credentials!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                        ;
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else {


                        if (response.getString("verified").equals("1")) {

                            Registration registration = new Registration();
                            registration.setStudentid(response.getString("studentid"));
                            registration.setEmail(response.getString("email"));
                            registration.setId(Integer.parseInt(response.getString("id")));
                            registration.setType(response.getString("type"));
                            new SessionHandling(LoginActivity.this).login(registration);


                        } else {

                            Intent intent = new Intent(LoginActivity.this, TokenEntryActivity.class);
                            intent.putExtra("email", response.getString("email"));
                            startActivity(intent);

                        }

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
