package com.example.dheeraj.location_tracking.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dheeraj on 14/09/17.
 */

public class DateUtil {

    public static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final ThreadLocal<SimpleDateFormat> ISO_DATETIME_FORMAT = new ThreadLocal<SimpleDateFormat>() { // NOPMD
        // by
        // dheerajtyagi
        // on
        // 20/12/16
        // 6:05
        // PM
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_DATETIME_PATTERN);
            return dateFormat;
        }
    };

    public static String getCurrentTimeInIsoFormat() {
        return ISO_DATETIME_FORMAT.get().format(new Date());
    }
}
