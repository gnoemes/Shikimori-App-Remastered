package com.gnoemes.shikimori.domain.rates

import io.reactivex.Completable
import io.reactivex.Observable

interface RateChangesInteractor  {
    fun getRateChanges() : Observable<Long>

    fun sendRateChanges(id : Long) : Completable
}