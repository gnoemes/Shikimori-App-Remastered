package com.gnoemes.shikimori.domain.rates

import com.gnoemes.shikimori.data.repository.rates.RateChangesRepository
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject


class RateChangesInteractorImpl @Inject constructor(
        private val repository: RateChangesRepository
) : RateChangesInteractor {

    override fun getRateChanges(): Observable<Long> = repository.getRateChanges().applyErrorHandlerAndSchedulers()

    override fun sendRateChanges(id: Long): Completable = repository.sendRateChanges(id).applyErrorHandlerAndSchedulers()
}