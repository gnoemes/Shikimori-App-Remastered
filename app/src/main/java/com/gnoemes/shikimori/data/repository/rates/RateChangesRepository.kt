package com.gnoemes.shikimori.data.repository.rates

import io.reactivex.Completable
import io.reactivex.Observable

interface RateChangesRepository {
    fun getRateChanges() : Observable<Long>

    fun sendRateChanges(id : Long) : Completable
}