package com.library.book;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.library.bean.Util;
import com.library.pojo.Book;

import java.util.Calendar;

public class GCMNotificationService extends IntentService {

    public GCMNotificationService() {
        super("GCMNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        SharedPreferences sharedpreferences = getSharedPreferences(
                Util.preference, Context.MODE_PRIVATE);


        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(intent.getExtras().getString("Library Management"))
                .setContentText(intent.getExtras().getString("message"))
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build();


        NotificationManager manager;
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent alertIntent = new Intent(this, BookDetailsActivity.class);
        alertIntent.putExtra("strbookid", intent.getExtras().getString("bookid"));

        alertIntent.putExtra("barcodeid", intent.getExtras().getString("barcodeid"));

        alertIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent1 = PendingIntent.getActivity(this, 0,
                alertIntent, 0);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.contentIntent = intent1;
        int index = (int) System.currentTimeMillis();

        manager.notify(index, notification);

        System.out.println("SET " +
                "ALARM" + intent.getExtras().getString("endtime"));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(intent.getExtras().getString("endtime")) * 1000);

        System.out.println(c.getTime());
        System.out.println(c.get(Calendar.HOUR));

       // new AppAlarmManager().SetAlarm(this, c, intent.getExtras().getString("bookid"), intent.getExtras().getString("barcodeid"));
    }

}
