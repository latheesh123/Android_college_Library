package com.library.book;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;

import java.util.HashMap;
import java.util.Map;

public class GCMRegistration {



    public static String getRegistrationId(SharedPreferences sharedPreferences) {
        String registrationId = sharedPreferences.getString("registrationId", "");
        if (registrationId.isEmpty()) {
            return "";
        }
        return registrationId;
    }

    GoogleCloudMessaging gcm;
    String registrationId;

    public String gcmregistration(final Context activity) {

        final SharedPreferences sharedpreferences = activity.getSharedPreferences(Util.preference, Context.MODE_PRIVATE);
        gcm = GoogleCloudMessaging.getInstance(activity);
        registrationId = getRegistrationId(sharedpreferences);
        if (registrationId.equals("")) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(activity);
                        }
                        registrationId = gcm.register("98661886605");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("registrationId", registrationId);
                        editor.commit();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {

                    new saveGcminDB(registrationId, sharedpreferences.getString(Util.userid, "")).execute();

                }
            }.execute(null, null, null);

        } else {

            new saveGcminDB(registrationId, sharedpreferences.getString(Util.userid, "")).execute();

        }
        return registrationId;
    }


    private class saveGcminDB extends AsyncTask<Void, Void, Void> {

        private String gcmid;
        private String userid;

        public saveGcminDB(String gcmid, String userid) {
            this.gcmid = gcmid;
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            Map<String, String> map = new HashMap<>();
            map.put("gcmid", gcmid);
            map.put("userid", userid);
            remoteConnection.getJSON(Util.maptoString(map), "SaveGcmServlet");
            return null;

        }
    }
}
