package com.library.bean;

import com.library.book.AdminLandingPageActivity;
import com.library.book.GCMRegistration;
import com.library.book.LoginActivity;
import com.library.book.UserLandingPageActivity;
import com.library.book.UserRegistrationActivity;
import com.library.pojo.Registration;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionHandling {

    Context context;
    public SharedPreferences sharedpreferences;

    public SessionHandling(Context context) {

        this.context = context;
        sharedpreferences = context.getSharedPreferences(
                Util.preference, Context.MODE_PRIVATE);
    }

    public void login(Registration dto) {
        Editor editor = sharedpreferences.edit();
        editor.putString(Util.studentname, dto.getStudentid());
        editor.putString(Util.userid, "" + dto.getId());
        editor.putString(Util.studentid, dto.getStudentid());
        editor.putString(Util.type, dto.getType());
        editor.commit();
        new GCMRegistration().gcmregistration(context);


        Intent intent;

        if (dto.getType().equals("1"))
            intent = new Intent(context, AdminLandingPageActivity.class);
        else
            intent = new Intent(context, UserLandingPageActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public void logout() {

        Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
         
    }
}
