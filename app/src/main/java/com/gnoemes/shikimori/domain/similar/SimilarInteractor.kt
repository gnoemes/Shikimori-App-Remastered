package com.gnoemes.shikimori.domain.similar

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.manga.domain.Manga
import io.reactivex.Single

interface SimilarInteractor {

    fun getAnimes(animeId: Long): Single<List<Anime>>

    fun getMangas(mangaId: Long): Single<List<Manga>>

    fun getRanobes(ranobeId: Long): Single<List<Manga>>

}