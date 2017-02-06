package com.library.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.library.bean.SessionHandling;

public class SettingsActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

    }


    public void logout(View view) {
        new SessionHandling(this).logout();
    }

    public void changepassword(View view) {

        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);

    }

    public void changeemail(View view) {

        Intent intent = new Intent(this, ChangeEmailActivity.class);
        startActivity(intent);

    }
}
