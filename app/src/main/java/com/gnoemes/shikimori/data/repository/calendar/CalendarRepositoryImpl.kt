package com.gnoemes.shikimori.data.repository.calendar

import com.gnoemes.shikimori.data.network.CalendarApi
import com.gnoemes.shikimori.data.repository.calendar.converter.CalendarResponseConverter
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import io.reactivex.Single
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
        private val api: CalendarApi,
        private val converter: CalendarResponseConverter
) : CalendarRepository {

    override fun getData(): Single<List<CalendarItem>> = api.getCalendar().map(converter)
}