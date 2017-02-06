package com.library.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.library.bean.Util;

public class ReturnBookActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        noback = true;
        super.onCreate(savedInstanceState);
        setTitle("Return Book");
        setContentView(R.layout.activity_return_book);
        System.out.println("fs");
    }

    public void scanQrcode(View view) {

        Intent intent = new Intent(this, QrCodeActivity.class);
        intent.putExtra("target", "returnbook");
        startActivity(intent);
    }

    public void scancodemanually(View view) {

        EditText enterbarcode = (EditText) findViewById(R.id.enterbarcode);

        if (enterbarcode.getText().toString().trim().equals(""))
            enterbarcode.setError("This field is required");

        else {

            new ReturnConfirmationTask(enterbarcode.getText().toString().trim(), this).execute(null, null, null);

        }
    }


}
