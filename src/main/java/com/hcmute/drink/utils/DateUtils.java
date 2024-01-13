package com.hcmute.drink.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date createBeginingOfDate() {
        Calendar calBegin = Calendar.getInstance();
        calBegin.set(Calendar.HOUR_OF_DAY, 0);
        calBegin.set(Calendar.MINUTE, 0);
        calBegin.set(Calendar.SECOND, 0);
        return calBegin.getTime();
    }

    public static Date createEndOfDate() {
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 23);
        calEnd.set(Calendar.MINUTE, 59);
        calEnd.set(Calendar.SECOND, 59);
        return calEnd.getTime();
    }
    public static Date createDateTimeByToday(int hour, int minute, int second, int millisecond, int numberDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, numberDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }
}
