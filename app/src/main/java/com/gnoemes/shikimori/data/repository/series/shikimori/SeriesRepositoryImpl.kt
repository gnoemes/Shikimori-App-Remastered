package com.gnoemes.shikimori.data.repository.series.shikimori

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.local.db.TranslationSettingDbSource
import com.gnoemes.shikimori.data.network.VideoApi
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.EpisodeResponseConverter
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.TranslationResponseConverter
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.VideoResponseConverter
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.series.domain.*
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
        private val api: VideoApi,
        private val converter: EpisodeResponseConverter,
        private val translationConverter: TranslationResponseConverter,
        private val videoConverter: VideoResponseConverter,
        private val episodeSource: EpisodeDbSource,
        private val syncSource: AnimeRateSyncDbSource,
        private val translationSettingSource: TranslationSettingDbSource
) : SeriesRepository {

    override fun getEpisodes(id: Long, alternative: Boolean): Single<List<Episode>> =
            (if (alternative) api.getEpisodesAlternative(id) else api.getEpisodes(id))
                    .map { it.sortedBy { it.index } }
                    .flatMap {
                        Observable.fromIterable(it)
                                .flatMapSingle { episode ->
                                    episodeSource.isEpisodeWatched(episode.animeId, episode.index)
                                            .map { isWatched -> converter.convertResponse(episode, isWatched) }
                                }
                                .toList()
                    }
                    .flatMap { episodes ->
                        episodeSource.saveEpisodes(episodes).toSingleDefault(episodes)
                                .flatMap { syncEpisodes(id, it) }
                    }

    override fun getTranslations(type: TranslationType, animeId: Long, episodeId: Long, alternative: Boolean): Single<List<Translation>> =
            (if (alternative) api.getTranslationsAlternative(animeId, episodeId, type.type!!) else api.getTranslations(animeId, episodeId, type.type!!))
                    .map(translationConverter)

    override fun getVideo(payload: TranslationVideo): Single<Video> =
            api.getVideo(
                    payload.animeId,
                    payload.episodeIndex,
                    if (payload.videoId == Constants.NO_ID) null else payload.videoId.toString(),
                    payload.language,
                    payload.type.type!!,
                    payload.authorSimple,
                    payload.videoHosting.synonymType
            )
                    .map(videoConverter)

    override fun getTranslationSettings(animeId: Long): Single<TranslationSetting> =
            translationSettingSource.getSetting(animeId)

    override fun saveTranslationSettings(settings: TranslationSetting): Completable =
            translationSettingSource.saveSetting(settings)

    override fun setEpisodeStatus(animeId: Long, episodeId: Int, isWatched: Boolean): Completable =
            if (isWatched) episodeSource.episodeWatched(animeId, episodeId)
            else episodeSource.episodeUnWatched(animeId, episodeId)

    override fun isEpisodeWatched(animeId: Long, episodeId: Int): Single<Boolean> = episodeSource.isEpisodeWatched(animeId, episodeId)

    private fun syncEpisodes(id: Long, list: List<Episode>): Single<List<Episode>> {
        return Single.fromCallable { list }
                .flatMap { episodes ->
                    Single.zip(episodeSource.getWatchedEpisodesCount(id), syncSource.getEpisodeCount(id), BiFunction<Int, Int, Boolean> { local, remote -> local == remote })
                            .flatMap { same -> if (same) Single.fromCallable { episodes } else updateFromSync(id, episodes) }
                }
    }

    private fun updateFromSync(id: Long, episodes: List<Episode>): Single<List<Episode>> {
        return Single.zip(episodeSource.getWatchedEpisodesCount(id), syncSource.getEpisodeCount(id), BiFunction<Int, Int, Int> { local, remote -> remote.minus(local) })
                .flatMap {
                    if (it > 0) increaseLocal(it, episodes)
                    else decreaseLocal(Math.abs(it), episodes)
                }
                .map { newStatusEpisodes ->
                    episodes.map { episode ->
                        newStatusEpisodes
                                .find { episode.index == it.index }
                                ?.let { episode.copy(isWatched = it.isWatched) }
                                ?: episode
                    }
                }
    }

    private fun decreaseLocal(count: Int, episodes: List<Episode>): Single<List<Episode>> =
            Observable.fromIterable(
                    episodes.asSequence()
                            .sortedByDescending { it.index }
                            .filter { it.isWatched }
                            .toMutableList()
                            .take(count))
                    .flatMapSingle { episodeSource.episodeUnWatched(it.animeId, it.index).toSingleDefault(it) }
                    .flatMapSingle { updateEpisode(it) }
                    .toList()


    private fun increaseLocal(count: Int, episodes: List<Episode>): Single<List<Episode>> =
            Observable.fromIterable(
                    episodes.asSequence()
                            .sortedBy { it.index }
                            .filter { !it.isWatched }
                            .toMutableList()
                            .take(count))
                    .flatMapSingle { episodeSource.episodeWatched(it.animeId, it.index).toSingleDefault(it) }
                    .flatMapSingle { updateEpisode(it) }
                    .toList()

    private fun updateEpisode(episode: Episode): Single<Episode> {
        return episodeSource.isEpisodeWatched(episode.animeId, episode.index)
                .map { episode.copy(isWatched = it) }
    }
}

