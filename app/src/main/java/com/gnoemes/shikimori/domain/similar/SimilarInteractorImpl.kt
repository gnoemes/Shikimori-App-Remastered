package com.gnoemes.shikimori.domain.similar

import com.gnoemes.shikimori.data.repository.anime.AnimeRepository
import com.gnoemes.shikimori.data.repository.manga.MangaRepository
import com.gnoemes.shikimori.data.repository.ranobe.RanobeRepository
import com.gnoemes.shikimori.data.repository.rates.RatesRepository
import com.gnoemes.shikimori.data.repository.user.UserRepository
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeWithStatus
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaWithStatus
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class SimilarInteractorImpl @Inject constructor(
        private val animeRepository: AnimeRepository,
        private val mangaRepository: MangaRepository,
        private val ranobeRepository: RanobeRepository,
        private val rateRepository: RatesRepository,
        private val userRepository: UserRepository
) : SimilarInteractor {

    override fun getAnimes(animeId: Long): Single<List<AnimeWithStatus>> =
            animeRepository.getSimilar(animeId)
                    .flatMap { list ->
                        if (userRepository.getUserStatus() == UserStatus.AUTHORIZED) mergeWithAnimeUserRates(list)
                        else Single.just(list.map { AnimeWithStatus(it) })
                    }
                    .applyErrorHandlerAndSchedulers()

    override fun getMangas(mangaId: Long): Single<List<MangaWithStatus>> =
            mangaRepository.getSimilar(mangaId)
                    .flatMap { list ->
                        if (userRepository.getUserStatus() == UserStatus.AUTHORIZED) mergeWithMangaUserRates(list)
                        else Single.just(list.map { MangaWithStatus(it) })
                    }
                    .applyErrorHandlerAndSchedulers()

    override fun getRanobes(ranobeId: Long): Single<List<MangaWithStatus>> =
            ranobeRepository.getSimilar(ranobeId)
                    .flatMap { list ->
                        if (userRepository.getUserStatus() == UserStatus.AUTHORIZED) mergeWithMangaUserRates(list)
                        else Single.just(list.map { MangaWithStatus(it) })
                    }
                    .applyErrorHandlerAndSchedulers()

    private fun mergeWithAnimeUserRates(animes: List<Anime>): Single<List<AnimeWithStatus>> =
            userRepository.getMyUserId()
                    .flatMap { rateRepository.getUserRates(it, target = Type.ANIME) }
                    .map { rates ->
                        val animeWithStatus = rates.filter { rate -> animes.find { it.id == rate.targetId } != null }
                        animes.map { item ->
                            val rate = animeWithStatus.find { item.id == it.targetId }
                            AnimeWithStatus(item, rate?.id, rate?.status)
                        }
                    }

    private fun mergeWithMangaUserRates(mangas: List<Manga>): Single<List<MangaWithStatus>> = userRepository.getMyUserId()
            .flatMap { rateRepository.getUserRates(it, target = Type.MANGA) }
            .map { rates ->
                val animeWithStatus = rates.filter { rate -> mangas.find { it.id == rate.targetId } != null }
                mangas.map { item ->
                    val rate = animeWithStatus.find { item.id == it.targetId }
                    MangaWithStatus(item, rate?.id, rate?.status)
                }
            }
}