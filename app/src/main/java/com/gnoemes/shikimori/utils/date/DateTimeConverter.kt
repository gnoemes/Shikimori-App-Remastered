package com.gnoemes.shikimori.utils.date

import org.joda.time.DateTime

interface DateTimeConverter {

    fun convertCalendarDateToString(dateTime: DateTime?): String

    fun convertAnimeSeasonToString(dateTime: DateTime?): String

    fun convertCommentDateTimeToString(dateTime: DateTime): String

    fun convertToFullHumanDateString(dateTime: DateTime?): String?

    fun convertHistoryDateToString(actionDate: DateTime): String

    fun convertDateAgoToString(actionDate: DateTime): String

    fun convertShortMonth(dateTime: DateTime) : String
}