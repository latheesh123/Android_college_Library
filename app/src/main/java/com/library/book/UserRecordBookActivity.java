package com.library.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UserRecordBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record_book);
        setTitle("Record Book");
    }


    public void scanQrcode(View view) {

        Intent intent = new Intent(this, QrCodeActivity.class);
        intent.putExtra("target", "getbookdetails");
        startActivity(intent);
    }

}
