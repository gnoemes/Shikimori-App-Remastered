package com.gnoemes.shikimori.data.repository.rates

import com.gnoemes.shikimori.data.local.db.PinnedRateDbSource
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.PinnedRate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PinnedRateRepositoryImpl @Inject constructor(
        private val source: PinnedRateDbSource
) : PinnedRateRepository {

    override fun getPinnedRates(type: Type, status: RateStatus): Single<List<PinnedRate>> = source.getPinnedRates(type, status)

    override fun addPinnedRate(rate: PinnedRate): Completable = source.addPinnedRate(rate)

    override fun removePinnedRate(id: Long): Completable = source.removePinnedRate(id)
}