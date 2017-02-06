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
import com.library.pojo.BookMarks;
import com.library.pojo.UserReservation;

import java.util.ArrayList;
import java.util.Calendar;

public class BookMarkAdapter extends ArrayAdapter<BookMarks> {

    private Activity activity;
    int layoutResourceId;
    private LayoutInflater inflater;
    BookMarks pojo;
    ArrayList<BookMarks> data;

    public BookMarkAdapter(Activity activity, int layoutResourceId, ArrayList<BookMarks> data) {
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
        convertView = inflater.inflate(R.layout.bookmark_row, parent, false);
        pojo = data.get(position);


        TextView pagenumber = (TextView) convertView.findViewById(R.id.pagenumber);
        pagenumber.setText("Page number : " + pojo.getPage());

        TextView location = (TextView) convertView.findViewById(R.id.location);
        location.setText("Location : " + pojo.getLocationname());

        return convertView;
    }
}
