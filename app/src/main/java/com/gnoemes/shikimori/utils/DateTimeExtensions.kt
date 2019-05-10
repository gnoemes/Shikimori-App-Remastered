package com.gnoemes.shikimori.utils

import org.joda.time.Duration
import org.joda.time.LocalTime
import org.joda.time.ReadablePartial
import org.joda.time.format.PeriodFormatterBuilder

fun Duration.toHoursAndMinutesAndSeconds(): String =
        PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSeparator(":")
                .minimumPrintedDigits(2)
                .appendMinutes()
                .appendSeparator(":")
                .minimumPrintedDigits(2)
                .appendSeconds()
                .toFormatter()
                .print(toPeriod())

fun Duration.toMinutesAndSeconds(): String =
        PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendMinutes()
                .appendSeparator(":")
                .minimumPrintedDigits(2)
                .appendSeconds()
                .toFormatter()
                .print(toPeriod())

fun LocalTime.isBeforeOrEqual(var1: ReadablePartial): Boolean {
    return this.isBefore(var1) || this.isEqual(var1)
}