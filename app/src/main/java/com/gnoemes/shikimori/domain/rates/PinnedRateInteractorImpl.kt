package com.gnoemes.shikimori.domain.rates

import com.gnoemes.shikimori.data.repository.rates.PinnedRateRepository
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.PinnedRate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PinnedRateInteractorImpl @Inject constructor(
        private val repository: PinnedRateRepository
) : PinnedRateInteractor {

    override fun getPinnedRates(type: Type, status: RateStatus): Single<List<PinnedRate>> = repository.getPinnedRates(type, status)
            .applyErrorHandlerAndSchedulers()

    override fun addPinnedRate(rate: PinnedRate): Completable = repository.addPinnedRate(rate)
            .applyErrorHandlerAndSchedulers()

    override fun removePinnedRate(id: Long): Completable = repository.removePinnedRate(id)
            .applyErrorHandlerAndSchedulers()
}