package com.gnoemes.shikimori.data.repository.calendar

import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import io.reactivex.Single

interface CalendarRepository {

    fun getData(): Single<List<CalendarItem>>
}