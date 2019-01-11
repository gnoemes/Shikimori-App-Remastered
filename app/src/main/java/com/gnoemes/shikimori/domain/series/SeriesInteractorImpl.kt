package com.gnoemes.shikimori.domain.series

import com.gnoemes.shikimori.data.repository.rates.RatesRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepository
import com.gnoemes.shikimori.data.repository.user.UserRepository
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.domain.Translation
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SeriesInteractorImpl @Inject constructor(
        private val repository: SeriesRepository,
        private val ratesRepository: RatesRepository,
        private val userRepository: UserRepository
) : SeriesInteractor {

    override fun getEpisodes(id: Long): Single<List<Episode>> = repository.getEpisodes(id).applyErrorHandlerAndSchedulers()

    override fun getTranslations(type: TranslationType, animeId: Long, episodeId: Int): Single<List<Translation>> =
            repository.getTranslations(type, animeId, episodeId)
                    .applyErrorHandlerAndSchedulers()

    override fun setEpisodeWatched(animeId: Long, episodeId: Int, rateId: Long): Completable =
            repository.isEpisodeWatched(animeId, episodeId)
                    .flatMapCompletable {
                        when (it) {
                            true -> updateRate(animeId, episodeId, rateId)
                            else -> Completable.complete()
                        }
                    }

    private fun updateRate(animeId: Long, episodeId: Int, rateId: Long): Completable =
            repository.setEpisodeWatched(animeId, episodeId)
                    .andThen(
                            when (rateId) {
                                Constants.NO_ID -> userRepository.getMyUserBrief()
                                        .flatMapCompletable { ratesRepository.createRate(animeId, Type.ANIME, UserRate(id = rateId, status = RateStatus.WATCHING), it.id) }
                                else -> ratesRepository.increment(rateId)
                            }
                    )

}