package com.gnoemes.shikimori.utils.date.impl;

import android.content.Context;

import com.gnoemes.shikimori.R;
import com.gnoemes.shikimori.utils.date.DateTimeResourceProvider;

import javax.inject.Inject;

public class DateTimeResourceProviderImpl implements DateTimeResourceProvider {

    private Context context;

    @Inject
    public DateTimeResourceProviderImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getTodayMessage() {
        return context.getString(R.string.common_today);
    }

    @Override
    public String getTomorrowMessage() {
        return context.getString(R.string.common_tomorrow);
    }

    @Override
    public String getYesterdayMessage() {
        return context.getString(R.string.common_yesterday);
    }

    @Override
    public String getCalendarDateFormat() {
        return context.getString(R.string.calendar_date_format);
    }

    @Override
    public String getFallString() {
        return context.getString(R.string.season_autumn);
    }

    @Override
    public String getWinterString() {
        return context.getString(R.string.season_winter);
    }

    @Override
    public String getSpringString() {
        return context.getString(R.string.season_spring);
    }

    @Override
    public String getSummerString() {
        return context.getString(R.string.season_summer);
    }

    @Override
    public String getThisWeekMessage() {
        return context.getString(R.string.common_this_week);
    }

    @Override
    public String getDaysAgoString(int days) {
        if (days == 1 || days == 0) {
            return context.getString(R.string.day_ago);
        }
        return context.getResources().getQuantityString(R.plurals.days_ago, days, days);
    }

    @Override
    public String getWeeksAgoString(int weeks) {
        if (weeks == 0) {
            return getThisWeekMessage();
        }
        if (weeks == 1) {
            return context.getString(R.string.week_ago);
        }
        return context.getResources().getQuantityString(R.plurals.weeks_ago, weeks, weeks);
    }

    @Override
    public String getMonthsAgoString(int months, boolean onlyWithNums) {
        if (months == 1) {
            return context.getString(R.string.month_ago);
        }
        if (months >= 6 && !onlyWithNums) {
            return context.getString(R.string.history_half_year_ago);
        }

        return context.getResources().getQuantityString(R.plurals.months_ago, months, months);
    }

    @Override
    public String getYearsAgoString(int years) {
        if (years == 1) {
            return context.getString(R.string.year_ago);
        }
        return context.getResources().getQuantityString(R.plurals.years_ago, years, years);
    }

    @Override
    public String getHoursAgoString(int hours) {
        if (hours == 1) {
            return context.getString(R.string.history_hour_ago);
        }
        return context.getResources().getQuantityString(R.plurals.hours_ago, hours, hours);
    }

    @Override
    public String getMinutesAgoString(int minutes) {
        if (minutes == 0) {
            return context.getString(R.string.history_its_now);
        }

        if (minutes == 1) {
            return context.getString(R.string.history_minute_ago);
        }

        return context.getResources().getQuantityString(R.plurals.minutes_ago, minutes, minutes);
    }
}
