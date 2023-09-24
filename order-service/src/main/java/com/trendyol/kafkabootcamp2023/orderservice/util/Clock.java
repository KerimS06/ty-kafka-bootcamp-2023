package com.trendyol.kafkabootcamp2023.orderservice.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public final class Clock {

    public static final DateTimeZone TURKEY_TIME_ZONE = DateTimeZone.forID("Europe/Istanbul");
    private static final String DATE_AND_TIME_DB_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    private static boolean isFrozen;
    private static DateTime timeSet;
    private static LocalDate timeAsLocalDate;

    private Clock() {
    }

    public static synchronized void freeze() {
        isFrozen = true;
    }

    public static synchronized void freeze(DateTime date) {
        freeze();
        setTime(date);
    }

    public static synchronized void unfreeze() {
        isFrozen = false;
        timeSet = null;
        timeAsLocalDate = null;
    }

    public static DateTime now() {
        DateTime dateTime = DateTime.now();
        if (isFrozen) {
            if (timeSet == null) {
                timeSet = dateTime;
            }

            return timeSet;
        } else {
            return dateTime;
        }
    }

    public static LocalDate nowAsLocalDate() {
        LocalDate localDate = LocalDate.now();
        if (isFrozen) {
            if (timeAsLocalDate == null) {
                timeAsLocalDate = localDate;
            }

            return timeAsLocalDate;
        } else {
            return localDate;
        }
    }

    public static synchronized void setTime(DateTime date) {
        timeSet = date;
        timeAsLocalDate = LocalDate.ofInstant(date.toDate().toInstant(), ZoneId.of("UTC"));
    }

    public static Date toTurkeyTimeZone(Date date) {
        return new DateTime(date.getTime(), TURKEY_TIME_ZONE)
                .toLocalDateTime()
                .toDate(TimeZone.getTimeZone("UTC"));
    }

    public static Long getTimeStamp() {
        return now().toInstant().getMillis();
    }

    public static String formatDate(Date date, String format) {
        if (Objects.isNull(date)) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static Date stringToDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_AND_TIME_DB_FORMAT);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getBeforeDateByDayCount(Integer dayCount) {
        return new Date(System.currentTimeMillis() - (dayCount * DAY_IN_MS));
    }
}

