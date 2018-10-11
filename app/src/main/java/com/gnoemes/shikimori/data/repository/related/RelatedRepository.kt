package com.gnoemes.shikimori.data.repository.related

import com.gnoemes.shikimori.entity.common.domain.Related
import io.reactivex.Single

interface RelatedRepository {

    fun getAnime(animeId: Long): Single<List<Related>>

    fun getManga(mangaId: Long): Single<List<Related>>

    fun getRanobe(mangaId: Long): Single<List<Related>>

}