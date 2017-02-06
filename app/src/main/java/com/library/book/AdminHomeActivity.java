package com.library.book;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.library.bean.SessionHandling;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Registration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.library.book.SimpleGestureFilter.SimpleGestureListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

public class AdminHomeActivity extends ParentActivity implements SimpleGestureListener{

    LocalActivityManager mLocalActivityManager;
    TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        noback = true;
        super.onCreate(savedInstanceState);
        detector = new SimpleGestureFilter(this,this);

        setTitle("Admin");
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AddBooksTask().execute(null, null, null);

            }
        });

         tabHost = (TabHost) findViewById(android.R.id.tabhost);

        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        tabHost.setup(mLocalActivityManager);


        TabHost.TabSpec recordBookSpec = tabHost.newTabSpec("IssueBook");
        TabHost.TabSpec listDateSpec = tabHost.newTabSpec("List By date");
        TabHost.TabSpec searchSpec = tabHost.newTabSpec("Search");
        TabHost.TabSpec returnSpec = tabHost.newTabSpec("Return");





        recordBookSpec.setIndicator("Issue Book").setContent(new Intent(this, RecordBookActivity.class));
        listDateSpec.setIndicator("List By Date").setContent(new Intent(this, ListByDateActivity.class));
        searchSpec.setIndicator("Search").setContent(new Intent(this, SearchBooksActivity.class));
        returnSpec.setIndicator("Return").setContent(new Intent(this, ReturnBookActivity.class));

        tabHost.addTab(recordBookSpec);
        tabHost.addTab(listDateSpec);
        tabHost.addTab(searchSpec);
        tabHost.addTab(returnSpec);

     //   if (getIntent().hasExtra("currenttab"))
       // {
         //   tabHost.setCurrentTab(getIntent().getIntExtra("currenttab",0));
       // }
        if (getIntent().hasExtra("currenttab")) {
            int currenttab = getIntent().getIntExtra("currenttab", 0);

            String title = "";
            if (currenttab == 0) {
                title = "Issue Book";
            } else if (currenttab == 1) {
                title = "List By date";
            } else if (currenttab == 2) {
                title = "Search";
            } else if (currenttab == 3) {
                title = "Return Book";
            }

            setTitle(title);
            tabHost.setCurrentTab(getIntent().getIntExtra("currenttab", 0));
        }

        tabHost.setOnTabChangedListener(new AnimatedTabHostListener(this, tabHost));


    }


    private SimpleGestureFilter detector;

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        String str = "";

        int currenttab = tabHost.getCurrentTab();
        switch (direction) {


            case SimpleGestureFilter.SWIPE_RIGHT :
                System.err.println(currenttab+"R");

                --currenttab;
                break;
            case SimpleGestureFilter.SWIPE_LEFT :

                   ++currenttab;
                break;


        }

        tabHost.setCurrentTab(currenttab);

       // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {
        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }

    protected void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();

    }

    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(isFinishing());

    }



    private class AddBooksTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                response = remoteConnection.getJSON(Util.maptoString(map), "AddBooksServlet");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Books Added", Toast.LENGTH_LONG).show();


        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(AdminHomeActivity.this, "Plhomeaease wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
