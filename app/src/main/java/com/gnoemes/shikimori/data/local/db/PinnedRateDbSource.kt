package com.gnoemes.shikimori.data.local.db

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.PinnedRate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import io.reactivex.Completable
import io.reactivex.Single


interface PinnedRateDbSource {

    fun getPinnedRates(type: Type, status: RateStatus): Single<List<PinnedRate>>

    fun addPinnedRate(rate: PinnedRate): Completable

    fun removePinnedRate(id: Long): Completable
}