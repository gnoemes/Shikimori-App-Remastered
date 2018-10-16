package com.gnoemes.shikimori.utils.date.impl

import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.date.DateTimeResourceProvider
import com.gnoemes.shikimori.utils.date.DateTimeUtils
import com.gnoemes.shikimori.utils.firstUpperCase
import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import javax.inject.Inject

//TODO shitty code, refactor sometime
class DateTimeConverterImpl @Inject constructor(
        private val resourceProvider: DateTimeResourceProvider,
        private val dateUtils: DateTimeUtils
) : DateTimeConverter {

    override fun convertCalendarDateToString(dateTime: DateTime?): String {
        val dateFormatter = DateTimeFormat.forPattern("d MMMM")
        val weekFormatter = DateTimeFormat.forPattern("EEEE")

        return when {
            dateUtils.isToday(dateTime) -> String.format(resourceProvider.calendarDateFormat, resourceProvider.todayMessage, dateFormatter.print(dateTime))
            dateUtils.isTomorrow(dateTime) -> String.format(resourceProvider.calendarDateFormat, resourceProvider.tomorrowMessage, dateFormatter.print(dateTime))
            else -> String.format(resourceProvider.calendarDateFormat, weekFormatter.print(dateTime).firstUpperCase(), dateFormatter.print(dateTime).firstUpperCase())
        }
    }

    override fun convertAnimeSeasonToString(dateTime: DateTime?): String {
        val season: String = when (dateTime?.monthOfYear) {
            1 -> resourceProvider.winterString
            2 -> resourceProvider.winterString
            3 -> resourceProvider.springString
            4 -> resourceProvider.springString
            5 -> resourceProvider.springString
            6 -> resourceProvider.summerString
            7 -> resourceProvider.summerString
            8 -> resourceProvider.summerString
            9 -> resourceProvider.fallString
            10 -> resourceProvider.fallString
            11 -> resourceProvider.fallString
            12 -> resourceProvider.winterString
            else -> return "xxx"
        }
        return "$season ${dateTime.year}"
    }

    override fun convertCommentDateTimeToString(dateTime: DateTime): String {
        val dateFormatter = DateTimeFormat.forPattern("dd MMMM yyyy")

        return when {
            dateUtils.isToday(dateTime) -> resourceProvider.todayMessage
            dateUtils.isYesterday(dateTime) -> resourceProvider.yesterdayMessage
            else -> dateFormatter.print(dateTime)
        }
    }

    override fun convertToFullHumanDateString(dateTime: DateTime): String {
        val dateFormatter = DateTimeFormat.forPattern("dd MMMM yyyy")
        return dateFormatter.print(dateTime)
    }

    override fun convertHistoryDateToString(actionDate: DateTime): String {
        return when {
            dateUtils.isToday(actionDate) -> resourceProvider.todayMessage
            dateUtils.isYesterday(actionDate) -> resourceProvider.yesterdayMessage
            Months.monthsBetween(dateUtils.nowDateTime, actionDate).months == 0 -> {
                val diff = Math.abs(dateUtils.nowDateTime.toLocalDate().weekOfWeekyear - actionDate.toLocalDate().weekOfWeekyear)
                resourceProvider.getWeeksAgoString(diff).firstUpperCase()!!
            }
            dateUtils.isSameYear(actionDate) || Years.yearsBetween(dateUtils.nowDateTime, actionDate).years == 0 -> {
                val diff = Math.abs(Months.monthsBetween(dateUtils.nowDateTime, actionDate).months)
                resourceProvider.getMonthsAgoString(diff, false).firstUpperCase()!!
            }
            else -> {
                val diff = Math.abs(Years.yearsBetween(dateUtils.nowDateTime, actionDate).years)
                resourceProvider.getYearsAgoString(diff).firstUpperCase()!!
            }
        }
    }

    override fun convertDateAgoToString(actionDate: DateTime): String {
        when {
            dateUtils.isSameDay(actionDate) -> {
                val hours = Math.abs(Hours.hoursBetween(dateUtils.nowDateTime, actionDate).hours)
                if (hours == 0) {
                    val minutes = Math.abs(Minutes.minutesBetween(dateUtils.nowDateTime, actionDate).minutes)
                    return resourceProvider.getMinutesAgoString(minutes)
                }
                return resourceProvider.getHoursAgoString(hours)
            }
            Months.monthsBetween(dateUtils.nowDateTime, actionDate).months == 0 -> {
                val diff = Math.abs(Days.daysBetween(dateUtils.nowDateTime, actionDate).days)
                return resourceProvider.getDaysAgoString(diff)
            }
            Years.yearsBetween(dateUtils.nowDateTime, actionDate).years == 0 -> {
                val diff = Math.abs(Months.monthsBetween(dateUtils.nowDateTime, actionDate).months)
                return resourceProvider.getMonthsAgoString(diff, true)
            }
            else -> {
                val diff = Math.abs(Years.yearsBetween(dateUtils.nowDateTime, actionDate).years)
                return resourceProvider.getYearsAgoString(diff)
            }
        }
    }
}