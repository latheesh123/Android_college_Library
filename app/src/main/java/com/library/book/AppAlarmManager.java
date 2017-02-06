package com.library.book;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;

import com.google.gson.Gson;
import com.library.bean.SessionHandling;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppAlarmManager extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {

        final SessionHandling sessionHandling = new SessionHandling(context);
        if(sessionHandling.sharedpreferences.getString(Util.userid, "").equals(""))
        {
            return;
        }
        //generatenotification(context, intent);
        AsyncTask<Void, Void, Void> task;

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                RemoteConnection remoteConnection = new RemoteConnection();
                Map<String, String> map = new HashMap<>();
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, -1);
                map.put("userid", sessionHandling.sharedpreferences.getString(Util.userid, ""));
                map.put("time", "" + c.getTimeInMillis() / 1000L);

                JSONArray response = remoteConnection.getJSON(Util.maptoString(map), "ReturnWarningServlet");

                for (int i = 0; i < response.length(); i++) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(i);

                        long endtime = jsonObject.getLong("endtime");

                        Calendar c1 = Calendar.getInstance();
                        c1.setTimeInMillis(endtime * 1000);
                        generatenotification(context, c1.getTime().toString(), jsonObject.getInt("bookid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                return null;
            }


        };

        task.execute(null, null, null);


    }

    private void generatenotification(Context context, Intent intent) {

        Notification notification = new NotificationCompat.Builder(context.getApplicationContext())
                .setContentTitle("Library management")
                .setContentText("Return book " + intent.getExtras().getString("time"))
                .setTicker("Return!")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notification_id = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, BookDetailsActivity.class);
        notificationIntent.putExtra("bookid", intent.getExtras().getString("bookid"));
        notificationIntent.putExtra("barcodeid", intent.getExtras().getString("barcodeid"));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent1 = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.contentIntent = intent1;

        mNotificationManager.notify(notification_id, notification);
    }

    private void generatenotification(Context context, String endtime, int id) {

        Notification notification = new NotificationCompat.Builder(context.getApplicationContext())
                .setContentTitle("Click here for details")
                .setContentText("You need to return the book on,  " + endtime)
                .setTicker("Return!")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notification_id = id;
        Intent notificationIntent = new Intent(context, BookDetailsActivity.class);
        System.out.println("BOOKID"+id);
        notificationIntent.putExtra("strbookid",""+ id);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent1 = PendingIntent.getActivity(context, uniqueInt,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.contentIntent = intent1;

        mNotificationManager.notify(notification_id, notification);
    }


    public void SetAlarm(Context context, Calendar c, String bookid, String barcodeid) {


        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AppAlarmManager.class);

        c.add(Calendar.DAY_OF_MONTH, -3);

        for (int i = 0; i < 3; i++) {

            System.out.println("SETTING ALAEM" + c.getTime());


            c.add(Calendar.DAY_OF_MONTH, 1);

            String formatted = Util.format1.format(c.getTime());
            intent.putExtra("time", formatted);
            intent.putExtra("bookid", bookid);
            intent.putExtra("barcodeid", barcodeid);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
        }
    }

    public void scheduledalarms(Activity context) {


        System.out.println("SETTING ALAEM");
        SharedPreferences sharedpreferences = new SessionHandling(context).sharedpreferences;

        Boolean set = sharedpreferences.getBoolean("set", false);
        if (!set) {


            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(context, AppAlarmManager.class);
            intent.putExtra("onetime", Boolean.FALSE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);


            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 2*60*1000, pi);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("set", true);
            editor.commit();
        }
    }


}