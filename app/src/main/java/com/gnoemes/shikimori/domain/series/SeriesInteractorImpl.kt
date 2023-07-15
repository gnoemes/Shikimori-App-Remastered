package com.gnoemes.shikimori.domain.series

import com.gnoemes.shikimori.data.repository.progress.AnimeProgressRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.EpisodeChangesRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepository
import com.gnoemes.shikimori.entity.series.domain.*
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class SeriesInteractorImpl @Inject constructor(
        private val repository: SeriesRepository,
        private val changesRepository: EpisodeChangesRepository,
        private val progressRepository: AnimeProgressRepository
) : SeriesInteractor {

    override fun getEpisodes(id: Long, name: String, alternative: Boolean): Single<List<Episode>> =
            repository.getEpisodes(id, name, alternative)
                    .map { list -> list.sortedBy { it.index } }
                    .applyErrorHandlerAndSchedulers()

    override fun getTranslations(type: TranslationType, animeId: Long, episodeId: Long, name : String, alternative: Boolean, loadLength: Boolean): Single<List<Translation>> =
            repository.getTranslations(type, animeId, episodeId, name, alternative, loadLength)
                    .applyErrorHandlerAndSchedulers()

    override fun getTranslationSettings(animeId: Long): Single<TranslationSetting> =
            progressRepository.getTranslationSettings(animeId)
                    .applyErrorHandlerAndSchedulers()

    override fun saveTranslationSettings(settings: TranslationSetting): Completable =
            progressRepository.saveTranslationSettings(settings)
                    .applyErrorHandlerAndSchedulers()

    override fun getVideo(payload: TranslationVideo, alternative: Boolean): Single<Video> =
            repository.getVideo(payload, alternative)
                    .applyErrorHandlerAndSchedulers()

    override fun getTopic(animeId: Long, episodeId: Int): Single<Long> =
            repository.getTopic(animeId, episodeId)
                    .applyErrorHandlerAndSchedulers()

    override fun getEpisodeChanges(): Observable<EpisodeChanges> = changesRepository.getEpisodesChanges().applyErrorHandlerAndSchedulers()
    override fun sendEpisodeChanges(changes: EpisodeChanges): Completable = changesRepository.sendEpisodeChanges(changes).applyErrorHandlerAndSchedulers()

    override fun getFirstNotWatchedEpisodeIndex(animeId: Long): Single<Int> =
            repository.getFirstNotWatchedEpisodeIndex(animeId)
                    .applyErrorHandlerAndSchedulers()

    override fun getWatchedEpisodesCount(animeId: Long): Single<Int> =
            repository.getWatchedEpisodesCount(animeId)
                    .applyErrorHandlerAndSchedulers()
}