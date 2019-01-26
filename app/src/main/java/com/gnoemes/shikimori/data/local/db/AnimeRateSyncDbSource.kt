package com.gnoemes.shikimori.data.local.db

import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.reactivex.Completable
import io.reactivex.Single

interface AnimeRateSyncDbSource {

    fun getRate(rateId : Long) : Single<UserRate>

    fun saveRate(userRate: UserRate): Completable

    fun getEpisodeCount(animeId: Long): Single<Int>

    fun clearRate(animeId: Long): Completable
}