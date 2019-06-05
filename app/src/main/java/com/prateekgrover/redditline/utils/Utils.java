package com.prateekgrover.redditline.utils;

import android.content.Context;

import com.prateekgrover.redditline.R;

import java.util.Calendar;

public class Utils {

    public static String getDaysAgo(long updateDateSec, Context context) {
        Calendar cal = Calendar.getInstance();
        final long timeDifference = cal.getTimeInMillis() - updateDateSec*1000;
        int diffDays = (int) (timeDifference / (86400000));
        if (diffDays < 0 || timeDifference < 0) {
            return context.getResources().getString(R.string.few_seconds_ago);
        } else {
            if (diffDays < 1 && diffDays != -1) {
                int hour = (int) (timeDifference / 3600000);
                if (hour < 1) {
                    int minute = (int) (timeDifference / 60000);
                    if (minute < 1) {
                        return context.getResources().getString(R.string.few_seconds_ago);
                    }
                    return context.getResources().getQuantityString(R.plurals.minute_ago, minute, minute);
                } else {
                    return context.getResources().getQuantityString(R.plurals.hour_ago, hour, hour);
                }
            } else {
                if (diffDays < 7) {
                    return context.getResources().getQuantityString(R.plurals.day_ago, diffDays, diffDays);
                } else if (diffDays <= 28) {
                    int s = diffDays / 7;
                    return context.getResources().getQuantityString(R.plurals.week_ago, s, s);
                } else {
                    if (diffDays < 365) {
                        int month = diffDays / 28;
                        return context.getResources().getQuantityString(R.plurals.month_ago, month, month);
                    } else {
                        int year = diffDays / 365;
                        return context.getResources().getQuantityString(R.plurals.year_ago, year, year);
                    }
                }
            }
        }
    }
}
