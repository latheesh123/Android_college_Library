package com.library.book;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.library.book.SimpleGestureFilter.SimpleGestureListener;

public class UserHomeActivity extends ParentActivity implements SimpleGestureListener{

    LocalActivityManager mLocalActivityManager;
    private SimpleGestureFilter detector;
    TabHost tabHost;

    @Override
    public void onPause() {
        super.onPause();
        try {
            mLocalActivityManager.dispatchPause(isFinishing());
        } catch (Exception e) {}
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mLocalActivityManager.dispatchResume();
        } catch (Exception e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        noback = true;

        super.onCreate(savedInstanceState);
        detector = new SimpleGestureFilter(this,this);
        setTitle("User");
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        tabHost = (TabHost) findViewById(android.R.id.tabhost);


        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        tabHost.setup(mLocalActivityManager);


        TabHost.TabSpec recordBookSpec = tabHost.newTabSpec("Check Out Books");
        TabHost.TabSpec favouritebooks = tabHost.newTabSpec("My Favourites");
        TabHost.TabSpec searchSpec = tabHost.newTabSpec("Search");
        TabHost.TabSpec mybooks = tabHost.newTabSpec("My Books");

        recordBookSpec.setIndicator("Check Out").setContent(new Intent(this, UserRecordBookActivity.class));
        mybooks.setIndicator("My Books").setContent(new Intent(this, MyBooksActivity.class));
        searchSpec.setIndicator("Search").setContent(new Intent(this, SearchBooksActivity.class));
        favouritebooks.setIndicator("My Favorite").setContent(new Intent(this, FavouriteBooksActivity.class));

        tabHost.addTab(recordBookSpec);
        tabHost.addTab(searchSpec);
        tabHost.addTab(mybooks);
        tabHost.addTab(favouritebooks);

        if (getIntent().hasExtra("currenttab")) {
            int currenttab = getIntent().getIntExtra("currenttab", 0);

            String title = "";
            if (currenttab == 0) {
                title = "Checkout Book";
            } else if (currenttab == 1) {
                title = "Search";
            } else if (currenttab == 2) {
                title = "My Books";
            } else if (currenttab == 3) {
                title = "Favourites";
            }

            setTitle(title);
            tabHost.setCurrentTab(getIntent().getIntExtra("currenttab", 0));
        }

        tabHost.setOnTabChangedListener(new AnimatedTabHostListener(this, tabHost));

    }

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
}
