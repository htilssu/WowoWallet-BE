package com.ewallet.ewallet.util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    public static String convertToString(Date date){
        return convertToString(date.toInstant());
    }

    public static String convertToString(Instant instant){
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }

    public static Date convertToDate(String date){
        return Date.from(DateTimeFormatter.ISO_INSTANT.parse(date, Instant::from));
    }

}
