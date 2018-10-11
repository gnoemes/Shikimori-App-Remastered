package com.gnoemes.shikimori.domain.rates

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.reactivex.Completable
import io.reactivex.Single

interface RatesInteractor {

    fun getAnimeRates(id: Long, page: Int, limit: Int, rateStatus: RateStatus): Single<List<Rate>>

    fun getMangaRates(id: Long, page: Int, limit: Int, rateStatus: RateStatus): Single<List<Rate>>

    fun deleteRate(id: Long): Completable

    fun createRate(id: Long, type: Type, rate: UserRate, userId: Long): Completable

    fun updateRate(rate: UserRate): Completable

    fun increment(rateId: Long): Completable

    fun decrement(rate: UserRate): Completable
}