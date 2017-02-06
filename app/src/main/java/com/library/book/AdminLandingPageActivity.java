package com.library.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class AdminLandingPageActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        noback = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing_page);
        setTitle("Librarian");
    }

    public void addsinglebook(View view) {

        Intent intent = new Intent(this, AddSingleBookActivity.class);
        startActivity(intent);

    }

    public void morebookstakengraph(View view) {

        Intent intent = new Intent(this, BooksTakenOrderActivity.class);
        startActivity(intent);
    }

    public void showchartsmenu(View view) {

        Intent intent = new Intent(this, ChartsMenuActivity.class);
        startActivity(intent);
    }


    public void btnaction(View view) {

        Intent intent = new Intent(this, AdminHomeActivity.class);
        int tab = 0;
        if (view.getId() == R.id.recordbook) {
            tab = 0;
        } else if (view.getId() == R.id.listbydate) {
            tab = 1;
        } else if (view.getId() == R.id.searchbook) {
            tab = 2;
        } else if (view.getId() == R.id.returnbook) {
            tab = 3;
        }
        intent.putExtra("currenttab", tab);
        startActivity(intent);

    }


    public void analysisreport(View view) {

        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);

    }

    public void userbooks(View view) {

        Intent intent = new Intent(this, UserBooksActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        return true;
    }


}
