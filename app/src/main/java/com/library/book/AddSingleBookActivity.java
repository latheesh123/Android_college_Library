package com.library.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddSingleBookActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_single_book);
        setTitle("Add single Book");
    }

    public void scanQrcode(View view) {

        Intent intent = new Intent(this, QrCodeActivity.class);
        intent.putExtra("target", "addsinglebook");
        startActivity(intent);
    }


}
