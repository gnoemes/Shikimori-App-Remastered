package com.gnoemes.shikimori.data.repository.rates

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RateChangesRepositoryImpl @Inject constructor(): RateChangesRepository {

    private val changeSource = PublishSubject.create<Long>()

    override fun getRateChanges(): Observable<Long> = changeSource

    override fun sendRateChanges(id: Long): Completable = Completable.fromAction {
        changeSource.onNext(id)
    }
}