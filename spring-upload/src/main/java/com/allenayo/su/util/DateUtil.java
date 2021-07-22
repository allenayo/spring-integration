package com.allenayo.su.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_WITH_LINE = "yyyy-MM-dd";
    public static final String YEAR_MONTH_DAY = "yyyyMMdd";
    public static final String HOUR_MINUTE_SECOND = "HH:mm:ss";

    public static String getSysDate(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    public static String getSysDate() {
        return getSysDate(YEAR_MONTH_DAY_WITH_LINE);
    }

    public static String getSysTime() {
        return getSysDate(HOUR_MINUTE_SECOND);
    }

    public static String getSysDateTime() {
        return getSysDate(YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
    }
}