package com.gnoemes.shikimori.data.repository.rates

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.ChapterDbSource
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.local.db.MangaRateSyncDbSource
import com.gnoemes.shikimori.data.network.UserApi
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
        private val api: UserApi,
        private val converter: RateResponseConverter,
        private val episodeDbSource: EpisodeDbSource,
        private val chapterDbSource: ChapterDbSource,
        private val animeSyncSource: AnimeRateSyncDbSource,
        private val mangaSyncSource: MangaRateSyncDbSource
) : RatesRepository {

    override fun getAnimeRates(id: Long, page: Int, limit: Int, rateStatus: RateStatus): Single<List<Rate>> =
            api.getUserAnimeRates(id, page, limit, rateStatus.status)
                    .map(converter)
                    .onErrorResumeNext { if (it is NoSuchElementException) Single.just(emptyList()) else Single.error(it) }
                    .doOnSuccess { if (it.isNotEmpty() && page > 1) it.toMutableList().removeAt(0) }

    override fun getMangaRates(id: Long, page: Int, limit: Int, rateStatus: RateStatus): Single<List<Rate>> =
            api.getUserMangaRates(id, page, limit, rateStatus.status)
                    .map(converter)
                    .onErrorResumeNext { if (it is NoSuchElementException) Single.just(emptyList()) else Single.error(it) }
                    .doOnSuccess { if (it.isNotEmpty() && page > 1) it.toMutableList().removeAt(0) }

    override fun createRate(id: Long, type: Type, rate: UserRate, userId: Long): Completable =
            when (type) {
                Type.ANIME -> createAnimeRate(id, rate, userId)
                Type.MANGA -> createMangaRate(id, rate, userId)
                Type.RANOBE -> createMangaRate(id, rate, userId)
                else -> Completable.error(IllegalStateException())
            }

    //Call only if my user
    override fun syncRate(id: Long): Completable =
            getRate(id)
                    .flatMapCompletable { syncRate(it) }

    override fun syncRate(rate: UserRate): Completable =
            when (rate.targetType) {
                Type.ANIME -> syncAnimeRate(rate)
                Type.MANGA, Type.RANOBE -> syncMangaRate(rate)
                else -> Completable.complete()
            }

    override fun updateRate(rate: UserRate): Completable =
            api.updateRate(rate.id!!, converter.convertCreateOrUpdateRequest(rate))

    override fun deleteRate(id: Long): Completable =
            getRate(id)
                    .flatMapCompletable { deleteRate(it) }
                    .andThen(api.deleteRate(id))

    override fun increment(rateId: Long): Completable = api.increment(rateId)

    private fun createAnimeRate(id: Long, rate: UserRate, userId: Long): Completable =
            episodeDbSource.getWatchedEpisodesCount(id)
                    .map {
                        rate.episodes = it
                        return@map converter.convertCreateOrUpdateRequest(id, Type.ANIME, rate, userId)
                    }
                    .flatMapCompletable { api.createRate(it) }

    private fun createMangaRate(id: Long, rate: UserRate, userId: Long): Completable =
            chapterDbSource.getReadedChapterCount(id)
                    .map {
                        rate.episodes = it
                        return@map converter.convertCreateOrUpdateRequest(id, Type.MANGA, rate, userId)
                    }
                    .flatMapCompletable { api.createRate(it) }

    override fun getRate(id: Long): Single<UserRate> =
            api.getRate(id)
                    .map { converter.convertUserRateResponse(null, it) }

    private fun syncAnimeRate(it: UserRate): Completable =
            Single.just(it)
                    .filter { it.targetId != null && it.episodes != null }
                    .flatMapCompletable { animeSyncSource.saveRate(it) }

    private fun syncMangaRate(it: UserRate): Completable =
            Single.just(it)
                    .filter { it.targetId != null && it.chapters != null }
                    .flatMapCompletable { mangaSyncSource.saveRate(it) }

    private fun deleteRate(rate: UserRate): Completable =
            when (rate.targetType) {
                Type.ANIME -> deleteAnimeRate(rate)
                Type.MANGA, Type.RANOBE -> deleteMangaRate(rate)
                else -> Completable.complete()
            }

    private fun deleteAnimeRate(rate: UserRate): Completable =
            Single.just(rate)
                    .filter { it.targetId != null }
                    .flatMapCompletable {
                        episodeDbSource.clearEpisodes(rate.targetId!!)
                                .andThen(animeSyncSource.clearRate(rate.targetId))
                    }

    private fun deleteMangaRate(rate: UserRate): Completable =
        Single.just(rate)
                .filter { it.targetId != null }
                .flatMapCompletable {
                    chapterDbSource.clearChapters(rate.targetId!!)
                            .andThen(mangaSyncSource.clearRate(rate.targetId))
                }



}
