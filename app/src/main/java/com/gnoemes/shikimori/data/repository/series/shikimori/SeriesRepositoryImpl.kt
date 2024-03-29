package com.gnoemes.shikimori.data.repository.series.shikimori

import com.gnoemes.shikimori.BuildConfig
import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.network.AnimeSource
import com.gnoemes.shikimori.data.network.TopicApi
import com.gnoemes.shikimori.data.network.VideoApi
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.EpisodeResponseConverter
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.TranslationResponseConverter
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.VideoResponseConverter
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.VkVideoConverter
import com.gnoemes.shikimori.data.repository.series.smotretanime.Anime365TokenSource
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.series.domain.*
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
        private val api: VideoApi,
        private val topicApi: TopicApi,
        private val source: AnimeSource,
        private val tokenSource: Anime365TokenSource,
        private val converter: EpisodeResponseConverter,
        private val translationConverter: TranslationResponseConverter,
        private val videoConverter: VideoResponseConverter,
        private val episodeSource: EpisodeDbSource,
        private val syncSource: AnimeRateSyncDbSource,
        private val vkConverter: VkVideoConverter
) : SeriesRepository {

    override fun getEpisodes(id: Long, name: String, alternative: Boolean): Single<List<Episode>> =
            (if (alternative) source.getEpisodesAlternative(id, name) else source.getEpisodes(id, name))
                    .map { episodes -> episodes.filter { it.index > 0 }.sortedBy { it.index } }
                    .map { episodes ->
                        if (alternative || tokenSource.getToken() != null) episodes
                        else episodes.filterNot { episode ->
                            episode.hostings.any { it is VideoHosting.SMOTRET_ANIME }
                        }
                    }
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

    override fun getTranslations(type: TranslationType, animeId: Long, episodeId: Long, name : String, alternative: Boolean): Single<List<Translation>> =
            (if (alternative) source.getTranslationsAlternative(animeId, name, episodeId, type) else source.getTranslations(animeId, name, episodeId, type))
                    .map(translationConverter)
                    .map { translations ->
                        if (alternative || tokenSource.getToken() != null) translations
                        else translations.filterNot { translation ->
                            translation.hosting is VideoHosting.SMOTRET_ANIME
                        }
                    }

    override fun getVideo(payload: TranslationVideo, alternative: Boolean): Single<Video> =
            (if (alternative) source.getVideoAlternative(payload.videoId, payload.animeId, payload.episodeIndex.toLong(), tokenSource.getToken())
            else source.getVideo(
                    payload.animeId,
                    payload.episodeIndex,
                    if (payload.videoId == Constants.NO_ID) "" else payload.videoId.toString(),
                    payload.language,
                    payload.type,
                    payload.authorSimple,
                    payload.videoHosting.synonymType,
                    payload.webPlayerUrl
            ))
                    .map(videoConverter)
                    .flatMap { if (it.hosting is VideoHosting.VK) getVkFiles(it) else Single.just(it) }

    private fun getVkFiles(video: Video): Single<Video> =
            api.getVkVideoFiles(BuildConfig.VkRandomToken, vkConverter.convertId(video))
                    .map { vkConverter.convertTracks(video, it) }

    override fun getTopic(animeId: Long, episodeId: Int): Single<Long> =
            topicApi.getAnimeEpisodeTopic(animeId, episodeId)
                    .map { list -> list.firstOrNull { it.episode?.toIntOrNull() == episodeId }?.id }

    override fun getFirstNotWatchedEpisodeIndex(animeId: Long): Single<Int> = episodeSource.getFirstNotWatchedEpisodeIndex(animeId)

    override fun getWatchedEpisodesCount(animeId: Long): Single<Int> = episodeSource.getWatchedEpisodesCount(animeId)

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

