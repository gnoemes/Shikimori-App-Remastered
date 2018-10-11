package com.gnoemes.shikimori.domain.similar

import com.gnoemes.shikimori.data.repository.anime.AnimeRepository
import com.gnoemes.shikimori.data.repository.manga.MangaRepository
import com.gnoemes.shikimori.data.repository.ranobe.RanobeRepository
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class SimilarInteractorImpl @Inject constructor(
        private val animeRepository: AnimeRepository,
        private val mangaRepository: MangaRepository,
        private val ranobeRepository: RanobeRepository
) : SimilarInteractor {

    override fun getAnimes(animeId: Long): Single<List<Anime>> =
            animeRepository.getSimilar(animeId)
                    .applyErrorHandlerAndSchedulers()

    override fun getMangas(mangaId: Long): Single<List<Manga>> =
            mangaRepository.getSimilar(mangaId)
                    .applyErrorHandlerAndSchedulers()

    override fun getRanobes(ranobeId: Long): Single<List<Manga>> =
            ranobeRepository.getSimilar(ranobeId)
                    .applyErrorHandlerAndSchedulers()
}