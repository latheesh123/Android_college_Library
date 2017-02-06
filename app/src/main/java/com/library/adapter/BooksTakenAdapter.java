package com.library.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.library.bean.Util;
import com.library.book.R;
import com.library.pojo.BooksTaken;
import com.library.pojo.UserReservation;

import java.util.ArrayList;
import java.util.Calendar;

public class BooksTakenAdapter extends ArrayAdapter<BooksTaken> {

    private Activity activity;
    int layoutResourceId;
    private LayoutInflater inflater;
    BooksTaken pojo;
    ArrayList<BooksTaken> data;

    public BooksTakenAdapter(Activity activity, int layoutResourceId, ArrayList<BooksTaken> data) {
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
        convertView = inflater.inflate(R.layout.books_taken_row, parent, false);
        pojo = data.get(position);


        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText("" + pojo.getDate());

        TextView count = (TextView) convertView.findViewById(R.id.count);
        count.setText("" + pojo.getTakencount());


        return convertView;
    }


}
