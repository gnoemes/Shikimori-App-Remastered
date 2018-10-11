package com.gnoemes.shikimori.domain.calendar

import com.gnoemes.shikimori.data.repository.calendar.CalendarRepository
import com.gnoemes.shikimori.data.repository.search.SearchRepository
import com.gnoemes.shikimori.domain.search.SearchQueryBuilder
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class CalendarInteractorImpl @Inject constructor(
        private val repository: CalendarRepository,
        private val searchRepository: SearchRepository,
        private val queryBuilder: SearchQueryBuilder
) : CalendarInteractor {

    override fun getCalendarData(): Single<List<CalendarItem>> =
            repository.getData().applyErrorHandlerAndSchedulers()

    override fun getMyCalendarData(): Single<List<CalendarItem>> =
            repository.getData()
                    .flatMap { items ->
                        val ids = items.asSequence().map { it.anime.id }.toMutableList()
                        Observable.concat(
                                queryBuilder.createMyListQueryFromIds(ids, RateStatus.WATCHING)
                                        .flatMap { searchRepository.getAnimeList(it) }
                                        .flatMapObservable { Observable.fromIterable(it) },
                                queryBuilder.createMyListQueryFromIds(ids, RateStatus.PLANNED)
                                        .flatMap { searchRepository.getAnimeList(it) }
                                        .flatMapObservable { Observable.fromIterable(it) })
                                .toList()
                                .map { myOngoings -> items.filter { item -> myOngoings.asSequence().map { it.id }.contains(item.anime.id) } }
                    }
                    .applyErrorHandlerAndSchedulers()
}