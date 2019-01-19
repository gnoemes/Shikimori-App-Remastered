package com.gnoemes.shikimori.data.repository.series.shikimori

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.network.VideoApi
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.EpisodeResponseConverter
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.TranslationResponseConverter
import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.entity.series.domain.Translation
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
        private val api: VideoApi,
        private val converter: EpisodeResponseConverter,
        private val translationConverter: TranslationResponseConverter,
        private val episodeSource: EpisodeDbSource,
        private val syncSource: AnimeRateSyncDbSource
) : SeriesRepository {

    override fun getEpisodes(id: Long): Single<List<Episode>> =
            api.getEpisodes(id)
                    .flatMap {
                        Observable.fromIterable(it)
                                .flatMapSingle { episode ->
                                    episodeSource.isEpisodeWatched(episode.animeId, episode.id)
                                            .map { isWatched -> converter.convertResponse(episode, isWatched) }
                                }
                                .toList()
                    }
                    .flatMap { episodes ->
                        episodeSource.saveEpisodes(episodes).toSingleDefault(episodes)
                                .flatMap { syncEpisodes(id, it) }
                    }

    override fun getTranslations(type: TranslationType, animeId: Long, episodeId: Int): Single<List<Translation>> =
            api.getTranslations(animeId, episodeId, type)
                    .map(translationConverter)

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
                                .find { episode.id == it.id }
                                ?.let { episode.copy(isWatched = it.isWatched) }
                                ?: episode
                    }
                }
    }

    private fun decreaseLocal(count: Int, episodes: List<Episode>): Single<List<Episode>> =
            Observable.fromIterable(
                    episodes.asSequence()
                            .sortedByDescending { it.id }
                            .filter { it.isWatched }
                            .toMutableList()
                            .take(count))
                    .flatMapSingle { episodeSource.episodeUnWatched(it.animeId, it.id).toSingleDefault(it) }
                    .flatMapSingle { updateEpisode(it) }
                    .toList()


    private fun increaseLocal(count: Int, episodes: List<Episode>): Single<List<Episode>> =
            Observable.fromIterable(
                    episodes.asSequence()
                            .sortedBy { it.id }
                            .filter { !it.isWatched }
                            .toMutableList()
                            .take(count))
                    .flatMapSingle { episodeSource.episodeWatched(it.animeId, it.id).toSingleDefault(it) }
                    .flatMapSingle { updateEpisode(it) }
                    .toList()

    private fun updateEpisode(episode: Episode): Single<Episode> {
        return episodeSource.isEpisodeWatched(episode.animeId, episode.id)
                .map { episode.copy(isWatched = it) }
    }
}

