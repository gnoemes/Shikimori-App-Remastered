package com.gnoemes.shikimori.data.repository.calendar

import com.gnoemes.shikimori.data.network.CalendarApi
import com.gnoemes.shikimori.data.network.UserApi
import com.gnoemes.shikimori.data.repository.calendar.converter.CalendarResponseConverter
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.reactivex.Single
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
        private val api: CalendarApi,
        private val userApi: UserApi,
        private val converter: CalendarResponseConverter,
        private val rateConverter: RateResponseConverter
) : CalendarRepository {

    override fun getData(): Single<List<CalendarItem>> = api.getCalendar().map(converter)

    override fun getCalendarRates(userId: Long): Single<List<UserRate>> = userApi
            .getUserRates(userId, targetType = "Anime", status = "${RateStatus.WATCHING.status},${RateStatus.PLANNED.status},${RateStatus.ON_HOLD.status}")
            .map { list -> list.mapNotNull { rateConverter.convertUserRateResponse(null, it) } }


}