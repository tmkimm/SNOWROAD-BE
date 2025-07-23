package com.snowroad.util;


import java.sql.Date;
import java.text.SimpleDateFormat;

public class H2Functions {

    public static Date strToDate(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            java.util.Date parsed = sdf.parse(date);
            return new Date(parsed.getTime());
        } catch (Exception e) {
            return null;
        }
    }
}
