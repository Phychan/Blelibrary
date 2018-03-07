package com.phychan.mylibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 陈晖 on 2017/4/22.
 */

public class TimeUtil {

    /*
      * 将时间转换为时间戳
      */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间(无时分秒)
     */
    public static String stampToDateWithoutLittle(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳转为时分秒
     */
    public static String getHMS(String timetemp) {
        long timeTemp = Long.parseLong(timetemp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeTemp);
        SimpleDateFormat fmat = new SimpleDateFormat("HH:mm:ss");
        String time = fmat.format(calendar.getTime());
        return time;
    }
}

