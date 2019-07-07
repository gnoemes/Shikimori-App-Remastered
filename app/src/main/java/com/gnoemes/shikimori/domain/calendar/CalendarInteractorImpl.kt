package com.gnoemes.shikimori.domain.calendar

import com.gnoemes.shikimori.data.repository.calendar.CalendarRepository
import com.gnoemes.shikimori.data.repository.user.UserRepository
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class CalendarInteractorImpl @Inject constructor(
        private val userRepository: UserRepository,
        private val repository: CalendarRepository
) : CalendarInteractor {

    override fun getCalendarData(): Single<List<CalendarItem>> =
            repository.getData()
                    .flatMap {
                        if (userRepository.getUserStatus() == UserStatus.AUTHORIZED) mergeWithUserRates(it)
                        else Single.just(it)
                    }
                    .applyErrorHandlerAndSchedulers()

    private fun mergeWithUserRates(calendar: List<CalendarItem>): Single<List<CalendarItem>> =
            userRepository.getMyUserId()
                    .flatMap { repository.getCalendarRates(it) }
                    .map { rates ->
                        val animeWithStatus = rates.filter { rate -> calendar.find { it.anime.id == rate.targetId } != null }
                        calendar.map { item ->
                                    val rate = animeWithStatus.find { item.anime.id == it.targetId }
                                    if (rate != null) item.copy(status = rate.status)
                                    else item
                                }
                    }
}