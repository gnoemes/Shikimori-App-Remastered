package com.gnoemes.shikimori.utils.date.impl;

import com.gnoemes.shikimori.utils.date.DateTimeUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.inject.Inject;

public class DateTimeUtilsImpl implements DateTimeUtils {

    @Inject
    public DateTimeUtilsImpl() {
    }

    @Override
    public DateTime getNowDateTime() {
        return DateTime.now();
    }

    @Override
    public boolean isToday(DateTime date) {
        return getNowDateTime().toLocalDate().equals(new LocalDate(date));
    }

    @Override
    public boolean isTomorrow(DateTime date) {
        return getNowDateTime().toLocalDate().plusDays(1).equals(new LocalDate(date));
    }

    @Override
    public boolean isYesterday(DateTime date) {
        return getNowDateTime().toLocalDate().minusDays(1).equals(new LocalDate(date));
    }

    @Override
    public boolean isSameWeek(DateTime time) {
        return getNowDateTime().toLocalDate().getWeekOfWeekyear() == time.toLocalDate().getWeekOfWeekyear();
    }

    @Override
    public boolean isSameWeek(DateTime firstDate, DateTime secondDate) {
        return firstDate.toLocalDate().getWeekOfWeekyear() == secondDate.toLocalDate().getWeekOfWeekyear();
    }

    @Override
    public boolean isSameMonth(DateTime time) {
        return getNowDateTime().toLocalDate().getMonthOfYear() == time.toLocalDate().getMonthOfYear();
    }

    @Override
    public boolean isSameMonth(DateTime firstDate, DateTime secondDate) {
        return firstDate.toLocalDate().getMonthOfYear() == secondDate.toLocalDate().getMonthOfYear();
    }

    @Override
    public boolean isSameYear(DateTime time) {
        return getNowDateTime().toLocalDate().getYear() == time.toLocalDate().getYear();
    }

    @Override
    public boolean isSameYear(DateTime firstDate, DateTime secondDate) {
        return firstDate.toLocalDate().getYear() == secondDate.toLocalDate().getYear();
    }

    @Override
    public boolean isSameDay(DateTime firstDate) {
        return getNowDateTime().toLocalDate().getDayOfYear() == firstDate.toLocalDate().getDayOfYear();
    }

    @Override
    public boolean isSameDay(DateTime firstDate, DateTime secondDate) {
        return firstDate != null &&
                secondDate != null &&
                firstDate.dayOfYear().get() == secondDate.dayOfYear().get();
    }
}
