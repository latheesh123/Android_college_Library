package com.library.bean;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Util {

    public static String preference = "library.management.pref";
    public static String studentid = "studentid";
    public static String userid = "userid";
    public static String studentname = "studentname";
    public static String type = "type";
    public static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");


    public static int NOSTATUS = 0;
    public static int RESERVED = 1;
    public static int CANCELLED = 2;
    public static int ISSUED = 3;
    public static int RETURNED = 4;


    public static String maptoString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    encodeString(entry.getKey()),
                    encodeString(entry.getValue())
            ));
        }
        return sb.toString();
    }

    public static String humanReadableDate(long unixtime) {

        Date date = new Date(unixtime);
        String formattedDate = format1.format(date);
        return formattedDate;
    }

    public static String encodeString(String s) {
        try {

            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
