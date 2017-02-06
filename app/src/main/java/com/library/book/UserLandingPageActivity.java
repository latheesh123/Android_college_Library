package com.library.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class UserLandingPageActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        noback = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing_page);
        setTitle("User");
        new AppAlarmManager().scheduledalarms(this);

    }


    public void settings(View view) {

    }

    public void btnaction(View view) {

        Intent intent = new Intent(this, UserHomeActivity.class);
        int tab = 0;
        if (view.getId() == R.id.recordbook) {
            tab = 0;
        } else if (view.getId() == R.id.searchbook) {
            tab = 1;
        } else if (view.getId() == R.id.mybooks) {
            tab = 2;
        } else if (view.getId() == R.id.favouriteList) {
            tab = 3;
        }
        intent.putExtra("currenttab", tab);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
       // getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }


}
