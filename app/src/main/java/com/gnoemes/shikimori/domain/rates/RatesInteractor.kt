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

    fun getRate(id : Long) : Single<UserRate>

    fun syncRate(id : Long) : Completable

    fun deleteRate(id: Long): Completable

    fun createRate(id: Long, type: Type, rate: UserRate, userId: Long): Completable

    fun createRateWithResult(id: Long, type: Type, status: RateStatus) : Single<UserRate>

    fun updateRate(rate: UserRate): Completable

    fun increment(rateId: Long): Completable

    fun decrement(rate: UserRate): Completable

    fun changeRateStatus(rateId: Long, newStatus: RateStatus): Completable
}