package com.gnoemes.shikimori.domain.related

import com.gnoemes.shikimori.entity.common.domain.Related
import io.reactivex.Single

interface RelatedInteractor {

    fun getAnime(animeId: Long): Single<List<Related>>

    fun getManga(mangaId: Long): Single<List<Related>>

    fun getRanobe(mangaId: Long): Single<List<Related>>
}