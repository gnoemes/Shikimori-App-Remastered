package com.gnoemes.shikimori.data.local.db

import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.reactivex.Completable
import io.reactivex.Single

interface MangaRateSyncDbSource {

    fun saveRate(userRate: UserRate): Completable

    fun getChaptersCount(mangaId: Long): Single<Int>

    fun clearRate(mangaId: Long): Completable
}