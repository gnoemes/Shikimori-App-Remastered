package com.gnoemes.shikimori.presentation.presenter.userhistory.conveter

import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.user.domain.UserHistory
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryHeaderViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryViewModel
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.date.DateTimeUtils
import org.joda.time.*
import javax.inject.Inject

class UserHistoryViewModelConverterImpl @Inject constructor(
        private val settings: SettingsSource,
        private val utils: DateTimeUtils,
        private val dateConverter: DateTimeConverter
) : UserHistoryViewModelConverter {

    override fun apply(t: List<UserHistory>): List<UserHistoryViewModel> =
            t.map { convertHistory(it) }

    private fun convertHistory(it: UserHistory): UserHistoryViewModel {
        val name = if (it.target is Anime) {
            if (settings.isRussianNaming) it.target.nameRu ?: it.target.name else it.target.name
        } else  {
            if (settings.isRussianNaming) (it.target as Manga).nameRu ?: it.target.name else it.target?.linkedName
        }

       return UserHistoryViewModel(
                it.id,
                it.description,
                dateConverter.convertDateAgoToString(it.dateCreated),
                it.dateCreated,
                name,
                it.target
        )
    }

    //TODO refactor?
    override fun groupItems(items: List<UserHistoryViewModel>): List<Any> {
        val newList = mutableListOf<Any>()

        var prevDate: DateTime? = null
        var prevWeeks = 0
        var prevMonths = 0
        val now = utils.nowDateTime
        items.forEach { item ->
            val hasDate = prevDate != null

            val days = !hasDate || (isSameWeek(item.actionDateTime, now) && (!utils.isSameDay(item.actionDateTime, prevDate) && isHalfWeek(item.actionDateTime, now) || (!isHalfWeek(item.actionDateTime, now) && isHalfWeek(now, prevDate!!))))
            val weeks = hasDate && (isSameMonth(item.actionDateTime, now) && (!isSameWeek(item.actionDateTime, now) && Weeks.weeksBetween(item.actionDateTime, now).weeks != prevWeeks))
            val months = hasDate && (isSameYear(item.actionDateTime, now) && (!isSameMonth(item.actionDateTime, now) && Months.monthsBetween(item.actionDateTime, now).months != prevMonths))
            val years = hasDate && !utils.isSameYear(item.actionDateTime, prevDate)

            val isNeedCategory = days || weeks || months || years

            if (isNeedCategory) {
                newList.add(convertHeader(item))
                prevDate = item.actionDateTime
            }

            prevWeeks = Weeks.weeksBetween(item.actionDateTime, now).weeks
            prevMonths = Months.monthsBetween(item.actionDateTime, now).months
            newList.add(item)
        }

        return newList
    }

    private fun convertHeader(item: UserHistoryViewModel): UserHistoryHeaderViewModel = UserHistoryHeaderViewModel(
            dateConverter.convertHistoryDateToString(item.actionDateTime)
    )

    private fun isHalfWeek(first: DateTime, second: DateTime): Boolean {
        return Math.abs(Days.daysBetween(first.toLocalDateTime(), second.toLocalDateTime()).days) < 2
    }


    private fun isSameWeek(first: DateTime, second: DateTime): Boolean = Weeks.weeksBetween(first.toLocalDateTime(), second.toLocalDateTime()).weeks == 0

    private fun isSameMonth(first: DateTime, second: DateTime): Boolean = Months.monthsBetween(first, second).months == 0

    private fun isSameYear(first: DateTime, second: DateTime): Boolean = Years.yearsBetween(first, second).years == 0

}