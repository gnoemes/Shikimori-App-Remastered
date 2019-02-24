package com.gnoemes.shikimori.domain.series

import com.gnoemes.shikimori.data.repository.series.shikimori.EpisodeChangesRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepository
import com.gnoemes.shikimori.data.repository.user.UserRepository
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.series.domain.*
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class SeriesInteractorImpl @Inject constructor(
        private val repository: SeriesRepository,
        private val ratesInteractor: RatesInteractor,
        private val userRepository: UserRepository,
        private val changesRepository: EpisodeChangesRepository
) : SeriesInteractor {

    override fun getEpisodes(id: Long, alternative: Boolean): Single<List<Episode>> = repository.getEpisodes(id, alternative).applyErrorHandlerAndSchedulers()

    override fun getTranslations(type: TranslationType, animeId: Long, episodeId: Long, alternative: Boolean): Single<List<Translation>> =
            repository.getTranslations(type, animeId, episodeId, alternative)
                    .applyErrorHandlerAndSchedulers()

    override fun getTranslationSettings(animeId: Long): Single<TranslationSetting> =
            repository.getTranslationSettings(animeId)
                    .applyErrorHandlerAndSchedulers()

    override fun saveTranslationSettings(settings: TranslationSetting): Completable =
            repository.saveTranslationSettings(settings)
                    .applyErrorHandlerAndSchedulers()

    override fun getVideo(payload: TranslationVideo, alternative: Boolean): Single<Video> =
            repository.getVideo(payload, alternative)
                    .applyErrorHandlerAndSchedulers()

    override fun getTopic(animeId: Long, episodeId: Int): Single<Long> =
            repository.getTopic(animeId, episodeId)
                    .applyErrorHandlerAndSchedulers()

    override fun getEpisodeChanges(): Observable<EpisodeChanges> = changesRepository.getEpisodesChanges().applyErrorHandlerAndSchedulers()
    override fun sendEpisodeChanges(changes: EpisodeChanges): Completable = changesRepository.sendEpisodeChanges(changes).applyErrorHandlerAndSchedulers()

    override fun setEpisodeStatus(animeId: Long, episodeId: Int, rateId: Long, isWatching: Boolean, onlyLocal: Boolean): Completable {
        return if (isWatching) setEpisodeWatched(animeId, episodeId, rateId, onlyLocal)
        else setEpisodeUnwatched(animeId, episodeId, rateId, onlyLocal)
    }

    override fun setEpisodeWatched(animeId: Long, episodeId: Int, rateId: Long, onlyLocal: Boolean): Completable =
            repository.isEpisodeWatched(animeId, episodeId)
                    .flatMapCompletable {
                        if (!it) updateRate(animeId, episodeId, rateId, true, onlyLocal)
                        else Completable.complete()
                    }
                    .applyErrorHandlerAndSchedulers()

    override fun setEpisodeUnwatched(animeId: Long, episodeId: Int, rateId: Long, onlyLocal: Boolean): Completable =
            repository.isEpisodeWatched(animeId, episodeId)
                    .flatMapCompletable {
                        if (it) updateRate(animeId, episodeId, rateId, false, onlyLocal)
                        else Completable.complete()
                    }.applyErrorHandlerAndSchedulers()


    private fun updateRate(animeId: Long, episodeId: Int, rateId: Long, isWatched: Boolean, onlyLocal: Boolean): Completable =
            repository.setEpisodeStatus(animeId, episodeId, isWatched)
                    .andThen(
                            if (!onlyLocal) {
                                if (isWatched) incrementOrCreate(animeId, rateId)
                                else decrement(rateId)
                            } else Completable.complete()
                    )

    private fun decrement(rateId: Long): Completable {
        return ratesInteractor.getRate(rateId)
                .flatMapCompletable { ratesInteractor.decrement(it) }
    }

    private fun incrementOrCreate(animeId: Long, rateId: Long): Completable {
        return when (rateId) {
            Constants.NO_ID -> userRepository.getMyUserBrief()
                    .flatMapCompletable { ratesInteractor.createRate(animeId, Type.ANIME, UserRate(id = rateId, status = RateStatus.WATCHING), it.id) }
            else -> ratesInteractor.increment(UserRate(rateId, targetType = Type.ANIME, targetId = animeId))
        }
    }

}