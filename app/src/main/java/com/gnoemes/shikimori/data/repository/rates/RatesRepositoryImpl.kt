package com.gnoemes.shikimori.data.repository.rates

import com.gnoemes.shikimori.data.local.db.ChapterDbSource
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
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
        private val chapterDbSource: ChapterDbSource
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

    override fun updateRate(rate: UserRate): Completable =
            api.updateRate(rate.id!!, converter.convertCreateOrUpdateRequest(rate))

    override fun deleteRate(id: Long): Completable = api.deleteRate(id)

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
                    .map { converter.convertUserRateResponse(it) }
}
