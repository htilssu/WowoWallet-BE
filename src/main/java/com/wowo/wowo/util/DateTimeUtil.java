package com.wowo.wowo.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    public static String convertToString(Date date) {
        return convertToString(date.toInstant());
    }

    public static String convertToString(Instant instant) {
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }

    public static Date convertToDate(String date) {
        return Date.from(DateTimeFormatter.ISO_INSTANT.parse(date, Instant::from));
    }

    public static Instant convertSqlDateToInstant(java.sql.Date date) {
        var localDate = date.toLocalDate();

        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

}
