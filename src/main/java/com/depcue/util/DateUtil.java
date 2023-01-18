package com.depcue.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static String FORMAT_DATE_MAX = "yyyy-MM-dd HH:mm:ss";


    public static Date getDateMax() {
        try {
            return new SimpleDateFormat(FORMAT_DATE_MAX).parse("2999-12-31 00:00:00");
        } catch (ParseException e) {
            return null;
        }
    }


}
