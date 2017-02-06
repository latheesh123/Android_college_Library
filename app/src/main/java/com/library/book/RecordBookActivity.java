package com.library.book;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Registration;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class RecordBookActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_book);
        setTitle("Checkout books");
    }

    public void scanQrcode(View view) {

        Intent intent = new Intent(this, QrCodeActivity.class);
        intent.putExtra("target", "reserveddetails");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println(requestCode);
        System.out.println(resultCode);

        if (requestCode == 2) {


        }
    }

}