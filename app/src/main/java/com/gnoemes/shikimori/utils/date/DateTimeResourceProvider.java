package com.gnoemes.shikimori.utils.date;

public interface DateTimeResourceProvider {

    /**
     * Returns "Today" string
     */
    String getTodayMessage();

    /**
     * Returns "Tomorrow" string
     */
    String getTomorrowMessage();

    /**
     * Returns format string
     * <p>
     * example: Today, 18 april
     */
    String getCalendarDateFormat();

    String getFallString();

    String getWinterString();

    String getSpringString();

    String getSummerString();

    String getYesterdayMessage();

    String getThisWeekMessage();

    String getDaysAgoString(int days);

    String getWeeksAgoString(int weeks);

    String getMonthsAgoString(int months, boolean onlyWithNums);

    String getYearsAgoString(int years);

    String getHoursAgoString(int hours);

    String getMinutesAgoString(int minutes);

    String getMonthString(int month);
}
