package com.library.adapter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.library.bean.Util;
import com.library.book.BookDetailsActivity;
import com.library.book.R;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;
import com.library.pojo.UserReservation;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReservedUsersAdapter extends ArrayAdapter<UserReservation> {

    private Activity activity;
    int layoutResourceId;
    private LayoutInflater inflater;
    UserReservation pojo;
    ArrayList<UserReservation> data;

    public ReservedUsersAdapter(Activity activity, int layoutResourceId, ArrayList<UserReservation> data) {
        super(activity, layoutResourceId, data);
        this.activity = activity;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = ((Activity) activity).getLayoutInflater();

        }
        convertView = inflater.inflate(R.layout.reserveduser_row, parent, false);
        pojo = data.get(position);


        TextView student_id = (TextView) convertView.findViewById(R.id.student_id);
        student_id.setText("Student id : " + pojo.getStudentid());

        TextView student_name = (TextView) convertView.findViewById(R.id.student_name);
        student_name.setText("Student Name : " + pojo.getUsername());


        TextView time = (TextView) convertView.findViewById(R.id.time);

        Calendar mydate = Calendar.getInstance();
        mydate.setTimeInMillis( Long.parseLong( pojo.getTime())*1000);

        time.setText("Time : " + mydate.get(Calendar.DAY_OF_MONTH) + "." + mydate.get(Calendar.MONTH) + "." + mydate.get(Calendar.YEAR));

        Button issueBook = (Button) convertView.findViewById(R.id.issueBook);
        issueBook.setTag(pojo);


        if (pojo.getStatus() == Util.ISSUED) {

            issueBook.setText("Issued");
            issueBook.setVisibility(View.VISIBLE);
        } else {

            issueBook.setVisibility(View.GONE);

        }

        issueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //     UserReservation reservation = (UserReservation) view.getTag();


            }
        });


        return convertView;
    }



}
