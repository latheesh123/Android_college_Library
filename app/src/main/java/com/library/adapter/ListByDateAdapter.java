package com.library.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.library.bean.Util;
import com.library.book.R;
import com.library.pojo.BookMarks;
import com.library.pojo.ListByDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListByDateAdapter extends ArrayAdapter<ListByDate> {

    private Activity activity;
    int layoutResourceId;
    private LayoutInflater inflater;
    ListByDate pojo;
    ArrayList<ListByDate> data;
    public long choosentime;

    public ListByDateAdapter(Activity activity, int layoutResourceId, ArrayList<ListByDate> data) {
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
        convertView = inflater.inflate(R.layout.listbydate_row, parent, false);
        pojo = data.get(position);


        TextView student_id = (TextView) convertView.findViewById(R.id.student_id);
        student_id.setText("Student Id : " + pojo.getStudentid());

        TextView student_name = (TextView) convertView.findViewById(R.id.student_name);
        student_name.setText("Student Name : " + pojo.getUsername());

        TextView bookname = (TextView) convertView.findViewById(R.id.bookname);
        bookname.setText("Book Title : " + pojo.getBookid());


        long endtime = 0;
        try {
            endtime = Long.parseLong(pojo.getEndtime()) * 1000L;

            TextView returndate = (TextView) convertView.findViewById(R.id.returndate);
            returndate.setText("Return Date : " + Util.humanReadableDate(endtime));

        } catch (Exception e) {
        }

        long starttime = 0;
        try {
            starttime = Long.parseLong(pojo.getTime()) * 1000L;

            TextView startdate = (TextView) convertView.findViewById(R.id.startdate);
            startdate.setText("Start Date : " + Util.humanReadableDate(starttime));

        } catch (Exception e) {
        }

        TextView status = (TextView) convertView.findViewById(R.id.status);

        if (issameday(starttime)) {

            status.setText("Book Issued");
            status.setBackgroundResource(R.color.brand_info);
        } else if (choosentime >= endtime) {

            status.setText("Missed Return");
            status.setBackgroundResource(R.color.brand_danger);

        } else {

            status.setText("Need to return");
            status.setBackgroundResource(R.color.brand_success);

        }


        return convertView;
    }

    public boolean issameday(long milliSeconds) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date getDate = calendar.getTime();
        calendar.setTimeInMillis(choosentime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startDate = calendar.getTime();

        return getDate.compareTo(startDate) == 0;

    }
}
