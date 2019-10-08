package com.gnoemes.shikimori.domain.similar

import com.gnoemes.shikimori.entity.anime.domain.AnimeWithStatus
import com.gnoemes.shikimori.entity.manga.domain.MangaWithStatus
import io.reactivex.Single

interface SimilarInteractor {

    fun getAnimes(animeId: Long): Single<List<AnimeWithStatus>>

    fun getMangas(mangaId: Long): Single<List<MangaWithStatus>>

    fun getRanobes(ranobeId: Long): Single<List<MangaWithStatus>>

}