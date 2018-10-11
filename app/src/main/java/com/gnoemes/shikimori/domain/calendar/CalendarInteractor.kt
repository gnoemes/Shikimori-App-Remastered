package com.gnoemes.shikimori.domain.calendar

import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import io.reactivex.Single

interface CalendarInteractor {

    fun getCalendarData(): Single<List<CalendarItem>>

    fun getMyCalendarData(): Single<List<CalendarItem>>
}