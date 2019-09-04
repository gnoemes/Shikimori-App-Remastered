package com.gnoemes.shikimori.data.repository.anime.converter

import com.gnoemes.shikimori.data.repository.common.GenreResponseConverter
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.data.repository.studio.StudioResponseConverter
import com.gnoemes.shikimori.entity.anime.data.AnimeDetailsResponse
import com.gnoemes.shikimori.entity.anime.data.AnimeVideoResponse
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.anime.domain.AnimeVideo
import com.gnoemes.shikimori.entity.user.data.StatisticResponse
import com.gnoemes.shikimori.entity.user.domain.Statistic
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class AnimeDetailsResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter,
        private val genreConverter: GenreResponseConverter,
        private val rateResponseConverter: RateResponseConverter,
        private val studioConverter: StudioResponseConverter
) : AnimeDetailsResponseConverter {

    override fun apply(t: AnimeDetailsResponse): AnimeDetails = AnimeDetails(
            t.id,
            t.name,
            t.nameRu,
            imageConverter.convertResponse(t.image),
            t.url.appendHostIfNeed(),
            t.type,
            t.status,
            t.episodes,
            t.episodesAired,
            t.dateAired,
            t.dateReleased,
            t.nextEpisodeDate,
            t.namesEnglish,
            t.namesJapanese,
            t.ageRating,
            t.score,
            t.duration,
            t.description,
            t.descriptionHtml,
            t.favoured,
            t.topicId,
            genreConverter.apply(t.genres),
            rateResponseConverter.convertUserRateResponse(t.id, t.userRate),
            convertVideos(t.videoResponses),
            studioConverter.apply(t.studioResponses ?: emptyList()),
            t.rateScoresStats.map { convertStatistic(it) },
            t.rateStatusesStats.map { convertStatistic(it) }
    )

    private fun convertStatistic(it: StatisticResponse): Statistic = Statistic(it.name, it.value)

    private fun convertVideos(videoResponses: List<AnimeVideoResponse>?): List<AnimeVideo>? {
        if (videoResponses == null) {
            return null
        }

        return videoResponses.map { AnimeVideo(it.id, it.name, it.imageUrl, it.url, it.type, it.hosting) }
    }
}