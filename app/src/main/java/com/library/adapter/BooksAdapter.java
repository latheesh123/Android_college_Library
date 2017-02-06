package com.library.adapter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.library.bean.Util;
import com.library.book.BookDetailsActivity;
import com.library.book.BookMarkDetailsActivity;
import com.library.book.LoginActivity;
import com.library.book.R;
import com.library.book.UserLocation;
import com.library.book.rest.RemoteConnection;
import com.library.pojo.Book;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BooksAdapter extends ArrayAdapter<Book> {

    private Activity activity;
    int layoutResourceId;
    private LayoutInflater inflater;
    Book book;
    ArrayList<Book> data;
    public ArrayList<Book> filterdata = new ArrayList<Book>();


    private int printstatus;
    private boolean mybooks = false;
    private boolean favouritebooks = false;


    public boolean isMybooks() {
        return mybooks;
    }

    public void setMybooks(boolean mybooks) {
        this.mybooks = mybooks;
    }

    public boolean isFavouritebooks() {
        return favouritebooks;
    }

    public void setFavouritebooks(boolean favouritebooks) {
        this.favouritebooks = favouritebooks;
    }

    SharedPreferences sharedpreferences;

    public BooksAdapter(Activity activity, int layoutResourceId, ArrayList<Book> data, int printstatus) {
        super(activity, layoutResourceId, data);

        this.activity = activity;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
        this.printstatus = printstatus;
        filterdata.addAll(data);

        sharedpreferences = activity.getSharedPreferences(
                Util.preference, Context.MODE_PRIVATE);

    }

    public void filter(String charText, ArrayList<String> searchby) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        System.out.println(charText.length());
        System.out.println(filterdata.size());

        if (charText.length() == 0) {
            data.addAll(filterdata);
        } else {
            for (Book wp : filterdata) {


                if (searchby.contains("Title")) {
                    if (wp.getTitle().toLowerCase().contains(charText)) {

                        data.add(wp);
                    }
                }

                if (searchby.contains("Author")) {
                    if (wp.getAuthors().toLowerCase().contains(charText)) {

                        if (!data.contains(wp))
                            data.add(wp);
                    }
                }
                if (searchby.contains("Year of publishing")) {
                    if (wp.getPublishedDate().toLowerCase().contains(charText)) {
                        if (!data.contains(wp))
                            data.add(wp);
                    }
                }

            }
        }
        System.out.println("FIlter" + data.size());
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = ((Activity) activity).getLayoutInflater();

        }
        convertView = inflater.inflate(R.layout.book_row, parent, false);
        book = data.get(position);


        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(activity).load(book.getSmallThumbnail()).into(image);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText("Title : " + book.getTitle());

        TextView publisher = (TextView) convertView.findViewById(R.id.publisher);
        publisher.setText("Publisher : " + book.getPublisher());

        TextView status = (TextView) convertView.findViewById(R.id.status);


        if (printstatus == 0) {
            status.setVisibility(View.GONE);
        } else {
            status.setVisibility(View.VISIBLE);
            String statuss = "NA";
            if (book.getStatus() == Util.ISSUED) {
                statuss = "Issued";
            }
            if (book.getStatus() == Util.CANCELLED) {
                statuss = "Cancelled";
            }
            if (book.getStatus() == Util.RESERVED) {
                statuss = "Reserved";
            }
            if (book.getStatus() == Util.RETURNED) {
                statuss = "Returned";
            }
            status.setText("Status : " + statuss);
        }


        Button details = (Button) convertView.findViewById(R.id.details);
        details.setTag(book);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Book book = (Book) view.getTag();
                Intent intent = new Intent(activity, BookDetailsActivity.class);
                intent.putExtra("bookid", book.getId());
                intent.putExtra("barcodeid", book.getBookid());

                activity.startActivity(intent);
            }
        });

        if (mybooks || sharedpreferences.getString(Util.type, "").equals("2")) {

            if (mybooks)
                details.setVisibility(View.GONE);
            ImageButton favouritebook = (ImageButton) convertView.findViewById(R.id.favouritebook);
            favouritebook.setTag(book);
                favouritebook.setVisibility(View.VISIBLE);

            favouritebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Book book = (Book) view.getTag();
                    new AddtoFavouriteTask("" + book.getId(), sharedpreferences.getString(Util.userid, "")).execute(null, null, null);
                }
            });
        }

        if (favouritebooks) {
            ImageButton favouritebook = (ImageButton) convertView.findViewById(R.id.favouritebook);
            favouritebook.setVisibility(View.GONE);

            details.setVisibility(View.GONE);
            Button bookmark = (Button) convertView.findViewById(R.id.bookmark);
            bookmark.setTag(book);
            bookmark.setVisibility(View.VISIBLE);
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Book book = (Book) view.getTag();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setTitle("Page number");
                    alertDialog.setMessage("Enter page number");

                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);

                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String pagenumber = input.getText().toString();

                                    new AddBookMarkTask("" + book.getId(), sharedpreferences.getString(Util.userid, ""), pagenumber).execute(null, null, null);
                                }
                            });

                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }
            });

            Button bookmarkdetails = (Button) convertView.findViewById(R.id.bookmarkdetails);
            bookmarkdetails.setTag(book);
            bookmarkdetails.setVisibility(View.VISIBLE);
            bookmarkdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Book bookmarkbook = (Book) view.getTag();
                    Intent intent = new Intent(activity, BookMarkDetailsActivity.class);
                    intent.putExtra("bookid", bookmarkbook.getId());
                    intent.putExtra("barcodeid", bookmarkbook.getBookid());
                    activity.startActivity(intent);
                }
            });


            if (book.getStatus() == 3) {
                bookmark.setVisibility(View.VISIBLE);
                bookmarkdetails.setVisibility(View.VISIBLE);

            }
            else {
                bookmark.setVisibility(View.GONE);
                bookmarkdetails.setVisibility(View.GONE);
            }


            final Button deletefavourite = (Button) convertView.findViewById(R.id.deletefavourite);
            deletefavourite.setTag(book);
            deletefavourite.setVisibility(View.VISIBLE);
            deletefavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Book deletebook = (Book) deletefavourite.getTag();

                    new DeleteFromFavouriteTask("" + book.getId(), sharedpreferences.getString(Util.userid, ""), deletebook).execute(null, null, null);

                }
            });


        }
        TextView returndate = (TextView) convertView.findViewById(R.id.returndate);
        System.out.println("START" + book.getStarttime());

        System.out.println("END" + book.getEndtime());
        if (book.getEndtime() != null && !book.getEndtime().equals("null") && !book.getEndtime().trim().equals("")) {
            returndate.setVisibility(View.VISIBLE);

            long endtime = 0;
            try {
                endtime = Long.parseLong(book.getEndtime()) * 1000L;

                returndate.setText("Return Date : " + Util.humanReadableDate(endtime));

            } catch (Exception e) {
            }
        } else {

            returndate.setVisibility(View.GONE);
        }
        TextView startdate = (TextView) convertView.findViewById(R.id.startdate);

        if (book.getStarttime() != null && !book.getStarttime().equals("null") && !book.getStarttime().trim().equals("")) {
            startdate.setVisibility(View.VISIBLE);

            long starttime = 0;
            try {
                starttime = Long.parseLong(book.getStarttime()) * 1000L;

                startdate.setText("Start Date : " + Util.humanReadableDate(starttime));

            } catch (Exception e) {
            }
        } else {
            startdate.setVisibility(View.GONE);

        }
        return convertView;
    }


    private class AddBookMarkTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;
        String bookid;
        String userid;
        String page;
        double latitude;
        double longitude;

        public AddBookMarkTask(String bookid, String userid, String page) {
            this.bookid = bookid;
            this.userid = userid;
            this.page = page;
            UserLocation locator = new UserLocation(activity);
            locator.getLocation();
            latitude = locator.getLatitude();
            longitude = locator.getLongitude();

        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("bookid", bookid);
                map.put("userid", userid);
                map.put("latitude", "" + latitude);
                map.put("longitude", "" + longitude);
                map.put("page", "" + page);

                response = remoteConnection.getJSON(Util.maptoString(map), "AddtoBookMarkServlet");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Toast.makeText(activity, "Book Mark Added", Toast.LENGTH_LONG).show();


        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(activity, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private class DeleteFromFavouriteTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;
        String bookid;
        String userid;
        Book book;


        public DeleteFromFavouriteTask(String bookid, String userid, Book book) {
            this.bookid = bookid;
            this.userid = userid;
            this.book = book;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("bookid", bookid);
                map.put("userid", userid);
                response = remoteConnection.getJSON(Util.maptoString(map), "DeleteFavouriteServlet");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Toast.makeText(activity, "Bookd Deleted from favourite list", Toast.LENGTH_LONG).show();
            data.remove(book);
            notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(activity, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class AddtoFavouriteTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;
        JSONArray response;
        String bookid;
        String userid;

        public AddtoFavouriteTask(String bookid, String userid) {
            this.bookid = bookid;
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RemoteConnection remoteConnection = new RemoteConnection();
            try {

                Map<String, String> map = new HashMap<>();
                map.put("bookid", bookid);
                map.put("userid", userid);
                response = remoteConnection.getJSON(Util.maptoString(map), "AddtoFavouriteServlet");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Toast.makeText(activity, "Bookd Added to favourite list", Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(activity, "Please wait ...", "", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
