package com.library.book;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.library.bean.Util;
import com.library.book.rest.RemoteConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QrCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        setTitle("Bar code");
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    ProgressDialog dialog;
    String bookid = null;
    boolean recieved = false;

    @Override
    public void handleResult(Result rawResult) {

        if (recieved) {
            return;
        }
        recieved = true;

        mScannerView.resumeCameraPreview(this);
      String barcoderesults = rawResult.getText();
       System.out.println("testing" + barcoderesults);
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("result", rawResult.getContents());
//        setResult(2,returnIntent);
//
//        finish();

        System.out.println("test" + getIntent().getStringExtra("target"));
        if (getIntent().getStringExtra("target").equals("reserveddetails")) {


            String result = barcoderesults;
            Intent intent = new Intent(this, ReservedDetailsActivity.class);
            intent.putExtra("bookid", result);
            startActivity(intent);
        } else if (getIntent().getStringExtra("target").equals("getbookdetails")) {

            final String barcoderesult = barcoderesults;

            System.out.println("Executing");
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {

                    RemoteConnection remoteConnection = new RemoteConnection();
                    Map<String, String> map = new HashMap<>();
                    map.put("barcode", barcoderesult);
                    try {
                        bookid = remoteConnection.getJSON(Util.maptoString(map), "GetBookIdServlet").getJSONObject(0).getString("bookid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    // TODO Auto-generated method stub
                    super.onPreExecute();
                    dialog = ProgressDialog.show(QrCodeActivity.this, "",
                            " Please wait...", true);

                }

                @Override
                protected void onPostExecute(Void result1) {
                    dialog.dismiss();

                    if (bookid != null) {
                        Intent intent = new Intent(QrCodeActivity.this, BookDetailsActivity.class);
                        intent.putExtra("bookid", Integer.parseInt(bookid));
                        intent.putExtra("barcodeid", barcoderesult);
                        startActivity(intent);
                    }

                    finish();
                }
            }.execute(null, null, null);

        } else if (getIntent().getStringExtra("target").equals("returnbook")) {

            String result = barcoderesults;
            new ReturnConfirmationTask(result, this).execute(null, null, null);
        }
        else if (getIntent().getStringExtra("target").equals("addsinglebook")) {

            String result = barcoderesults;
            new AddSingleBookThread(result, this).execute(null, null, null);

        }



    }


}