package com.gnoemes.shikimori.domain.series

import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepository
import com.gnoemes.shikimori.data.repository.user.UserRepository
import com.gnoemes.shikimori.domain.rates.RatesInteractor
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
        private val ratesInteractor: RatesInteractor,
        private val userRepository: UserRepository
) : SeriesInteractor {

    override fun getEpisodes(id: Long): Single<List<Episode>> = repository.getEpisodes(id).applyErrorHandlerAndSchedulers()

    override fun getTranslations(type: TranslationType, animeId: Long, episodeId: Int): Single<List<Translation>> =
            repository.getTranslations(type, animeId, episodeId)
                    .applyErrorHandlerAndSchedulers()

    override fun setEpisodeStatus(animeId: Long, episodeId: Int, rateId: Long, isWatching: Boolean): Completable {
        return if (isWatching) setEpisodeWatched(animeId, episodeId, rateId)
        else setEpisodeUnwatched(animeId, episodeId, rateId)
    }

    override fun setEpisodeWatched(animeId: Long, episodeId: Int, rateId: Long): Completable =
            repository.isEpisodeWatched(animeId, episodeId)
                    .flatMapCompletable {
                        if (!it) updateRate(animeId, episodeId, rateId, true)
                        else Completable.complete()
                    }
                    .applyErrorHandlerAndSchedulers()

    override fun setEpisodeUnwatched(animeId: Long, episodeId: Int, rateId: Long): Completable =
            repository.isEpisodeWatched(animeId, episodeId)
                    .flatMapCompletable {
                        if (it) updateRate(animeId, episodeId, rateId, false)
                        else Completable.complete()
                    }.applyErrorHandlerAndSchedulers()


    private fun updateRate(animeId: Long, episodeId: Int, rateId: Long, isWatched: Boolean): Completable =
            repository.setEpisodeStatus(animeId, episodeId, isWatched)
                    .andThen(
                            if (isWatched) incrementOrCreate(animeId, rateId)
                            else decrement(rateId)
                    ).andThen(ratesInteractor.syncRate(rateId))

    private fun decrement(rateId: Long): Completable {
        return ratesInteractor.getRate(rateId)
                .flatMapCompletable { ratesInteractor.decrement(it) }
    }

    private fun incrementOrCreate(animeId: Long, rateId: Long): Completable {
        return when (rateId) {
            Constants.NO_ID -> userRepository.getMyUserBrief()
                    .flatMapCompletable { ratesInteractor.createRate(animeId, Type.ANIME, UserRate(id = rateId, status = RateStatus.WATCHING), it.id) }
            else -> ratesInteractor.increment(rateId)
        }
    }

}