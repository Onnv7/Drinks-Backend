package com.hcmute.drink.utils;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class MongoDbUtils {
    public Date createCurrentDateTime(int hour, int minute, int second, int millisecond) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime currentDate = now.withHour(hour).withMinute(minute).withSecond(second).withNano(millisecond);
        return Date.from(currentDate.toInstant());
    }
    public Date createCurrentTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        return Date.from(now.toInstant());
    }
    public Date createPreviousDay(int hour, int minute, int second, int millisecond, int prevDay) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime previousDay = now.minusDays(prevDay).withHour(hour).withMinute(minute).withSecond(second).withNano(millisecond);
        return Date.from(previousDay.toInstant());
    }
}
