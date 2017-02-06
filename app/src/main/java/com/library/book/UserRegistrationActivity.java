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
import com.library.pojo.Registration;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class UserRegistrationActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        setTitle("User Registration");
    }

    public void btn_register(View view) {

        EditText email = (EditText) findViewById(R.id.email);
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        EditText studentid = (EditText) findViewById(R.id.studentid);

        if (email.getText().toString().trim().equals(""))
            email.setError("Please fill this");
        else if (username.getText().toString().trim().equals(""))
            username.setError("Please fill this");
        else if (password.getText().toString().trim().equals(""))
            password.setError("Please fill this");
        else if (studentid.getText().toString().trim().equals(""))
            studentid.setError("Please fill this");


        Registration registration = new Registration();
        registration.setEmail(email.getText().toString().trim());
        registration.setPassword(password.getText().toString().trim());
        registration.setStudentid(studentid.getText().toString().trim());
        registration.setUsername(username.getText().toString().trim());

        new RegistrationTask(registration, this).execute(null, null, null);
    }


    private class RegistrationTask extends AsyncTask<Void, Void, Void> {


        private Activity activity;
        private ProgressDialog progressDialog;
        JSONArray response;
        Registration registration;

        public RegistrationTask(Registration registration, Activity activity) {
            this.activity = activity;
            this.registration = registration;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("username", registration.getUsername());
                map.put("studentid", registration.getStudentid());
                map.put("email", registration.getEmail());
                map.put("password", registration.getPassword());


                response = remoteConnection.getJSON(Util.maptoString(map), "UserRegistrationServlet");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();

            if (response != null) {
                try {
                    if (response.getJSONObject(0).has("error")) {

                        new AlertDialog.Builder(activity)
                                .setTitle("Error")
                                .setMessage(response.getJSONObject(0).getString("error"))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })

                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();


                    } else {
                        new AlertDialog.Builder(activity)
                                .setTitle("Email verification")
                                .setMessage("OTP has been sent to your email !")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(activity, TokenEntryActivity.class);
                                        intent.putExtra("email", registration.getEmail());
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

            progressDialog = ProgressDialog.show(activity, "Please wait .", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
