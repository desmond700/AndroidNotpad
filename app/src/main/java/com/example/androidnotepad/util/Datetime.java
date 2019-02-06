package com.example.androidnotepad.util;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Datetime {
    private int seconds;
    private int minutes;
    private int hour;
    private int day;
    private int month;
    private int year;
    String mDate;
    String mTime;
    private final String[] MONTH = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    public Datetime() {
    }

    public Datetime(String dateString) {

        mDate = dateString.substring(0, dateString.indexOf(' '));
        mTime = dateString.substring(dateString.indexOf(' ')+1);
        int timeSeparatorFirst = mTime.indexOf(':');
        int timeSeparatorLast = mTime.lastIndexOf(':');

        int dateSeparatorFirst = mDate.indexOf('-');
        int dateSeparatorLast = mDate.lastIndexOf('-');

        seconds = Integer.parseInt(mTime.substring(timeSeparatorLast+1));
        minutes = Integer.parseInt(mTime.substring(timeSeparatorFirst+1, timeSeparatorLast));
        hour = Integer.parseInt(mTime.substring(0, mTime.indexOf(':')));
        day = Integer.parseInt(mDate.substring(dateSeparatorLast+1));
        month = Integer.parseInt(mDate.substring(dateSeparatorFirst+1, dateSeparatorLast));
        year =Integer.parseInt(mDate.substring(0, dateSeparatorFirst));
    }

    public Datetime(int seconds, int minutes, int hour, int day, int month, int year) {
        this.seconds = seconds;
        this.minutes = minutes;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }

    public String getMonth() {
        String monthStr = "";
        switch(month){
            case 1:
                monthStr = MONTH[0];
                break;
            case 2:
                monthStr = MONTH[1];
                break;
            case 3:
                monthStr = MONTH[2];
                break;
            case 4:
                monthStr = MONTH[3];
                break;
            case 5:
                monthStr = MONTH[4];
                break;
            case 6:
                monthStr = MONTH[5];
                break;
            case 7:
                monthStr = MONTH[6];
                break;
            case 8:
                monthStr = MONTH[7];
                break;
            case 9:
                monthStr = MONTH[8];
                break;
            case 10:
                monthStr = MONTH[9];
                break;
            case 11:
                monthStr = MONTH[10];
                break;
            case 12:
                monthStr = MONTH[11];
                break;
        }
        return monthStr;
    }

    public int getYear() {
        return year;
    }

    public String getDate() {
        return getMonth() + " " + getDay() + " " + getYear();
    }

    public String getTime() {
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(mTime);

            return _12HourSDF.format(_24HourDt);
        }catch(ParseException ex){
            Log.d("DateError",ex.getMessage());
        }
        return "";
    }
}
