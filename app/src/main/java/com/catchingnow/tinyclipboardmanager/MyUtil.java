package com.catchingnow.tinyclipboardmanager;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heruoxin on 15/3/4.
 */

public class MyUtil {
    public final static String PACKAGE_NAME = "tinyclipboardmanager";

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    public static String stringLengthCut(String string) {
        return stringLengthCut(string, 200);
    }

    public static String stringLengthCut(String string, int length) {
        string = string.trim();
        return  (string.length() > length) ?
                string.substring(0, length - 2).trim()+"…"
                : string.trim();
    }

    public static String getFormatDate(Context context, Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.date_format));
        return dateFormat.format(date);
    }

    public static String getFormatTime(Context context, Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.time_format));
        return dateFormat.format(date);
    }

    public static String getFormatTimeWithSecond(Context context, Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.time_format_with_second));
        return dateFormat.format(date);
    }

}
