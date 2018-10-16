package com.gnoemes.shikimori.utils.date;

import org.joda.time.DateTime;

/**
 * Util class for date and time
 */
public interface DateTimeUtils {

    /**
     * Returns current date
     */
    DateTime getNowDateTime();

    boolean isToday(DateTime time);

    boolean isTomorrow(DateTime time);

    boolean isYesterday(DateTime time);

    boolean isSameWeek(DateTime time);

    boolean isSameWeek(DateTime firstDate, DateTime secondDate);

    boolean isSameMonth(DateTime time);

    boolean isSameMonth(DateTime firstDate, DateTime secondDate);

    boolean isSameYear(DateTime time);

    boolean isSameYear(DateTime firstDate, DateTime secondDate);

    boolean isSameDay(DateTime firstDate);

    boolean isSameDay(DateTime firstDate, DateTime secondDate);

}