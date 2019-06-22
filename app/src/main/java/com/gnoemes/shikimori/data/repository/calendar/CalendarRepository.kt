package com.gnoemes.shikimori.data.repository.calendar

import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.reactivex.Single

interface CalendarRepository {

    fun getData(): Single<List<CalendarItem>>

    fun getCalendarRates(userId : Long) : Single<List<UserRate>>
}