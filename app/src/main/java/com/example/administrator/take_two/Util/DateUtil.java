package com.example.administrator.take_two.Util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/20.
 */
public class DateUtil {
    public static String getDateStr(int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date(cal.getTimeInMillis()));
    }

    public static long getDateMills(int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, monthOfYear, dayOfMonth);
        return cal.getTimeInMillis();
    }
    public static void datePicker(final Context mContext, final TextView curEdit, DatePickerDialog.OnDateSetListener dateSetListener) {

        String curDate = curEdit.getText().toString().trim();

        int year;
        int month;
        int day;

        if ("".equals(curDate)) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            try {
                String[] dateStrs = curDate.split("-");
                year = Integer.parseInt(dateStrs[0]);
                month = Integer.parseInt(dateStrs[1]) - 1;
                day = Integer.parseInt(dateStrs[2]);
            } catch (Exception e) {
                // TODO: handle exception
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            }
        }

        DatePickerDialog pickerDialog = new DatePickerDialog(
                mContext, dateSetListener, year, month, day);
        pickerDialog.show();
    }
    public static boolean smallThanCurrDate(int year, int month, int day) {
        //判断日期是否大于当前的日期
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int current_year = cal.get(Calendar.YEAR);
        int current_month = cal.get(Calendar.MONTH);
        int current_day = cal.get(Calendar.DAY_OF_MONTH);
        if (year <= current_year) {
            if (year == current_year) {
                if (month <= current_month) {
                    if (month == current_month) {
                        if (day > current_day)
                            return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static String getToDayDate() {
        Calendar currDate = Calendar.getInstance();
        currDate.clear();
        currDate.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date(currDate.getTimeInMillis()));
    }

    public static long getToDayMills(){
        return System.currentTimeMillis();
    }

    public static int getToDayYear(){
        Calendar currDate = Calendar.getInstance();
        currDate.clear();
        currDate.setTimeInMillis(System.currentTimeMillis());
        return currDate.get(Calendar.YEAR);
    }

    public static int getToDayMonth(){
        Calendar currDate = Calendar.getInstance();
        currDate.clear();
        currDate.setTimeInMillis(System.currentTimeMillis());
        return currDate.get(Calendar.MONTH)+1;
    }

    public static int getToDayDay(){
        Calendar currDate = Calendar.getInstance();
        currDate.clear();
        currDate.setTimeInMillis(System.currentTimeMillis());
        return currDate.get(Calendar.DAY_OF_MONTH);
    }
    /**
     * 输入长整型time
     * 根据对应的转换格式进行转换
     */
    public static String getStringFormat(long millis,String formatType){
        SimpleDateFormat sdf= new SimpleDateFormat(formatType);
        java.util.Date dt = new java.sql.Date(millis*1000);
        return sdf.format(dt);
    }
}
