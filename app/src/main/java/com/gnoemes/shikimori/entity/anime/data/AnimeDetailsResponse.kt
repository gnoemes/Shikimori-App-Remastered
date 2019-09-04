package com.gnoemes.shikimori.entity.anime.data

import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.data.GenreResponse
import com.gnoemes.shikimori.entity.common.data.ImageResponse
import com.gnoemes.shikimori.entity.common.domain.AgeRating
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.rates.data.UserRateResponse
import com.gnoemes.shikimori.entity.studio.StudioResponse
import com.gnoemes.shikimori.entity.user.data.StatisticResponse
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class AnimeDetailsResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("russian") val nameRu: String?,
        @field:SerializedName("image") val image: ImageResponse,
        @field:SerializedName("url") val url: String,
        @field:SerializedName("kind") private val _type: AnimeType?,
        @field:SerializedName("status") private val _status: Status?,
        @field:SerializedName("episodes") val episodes: Int,
        @field:SerializedName("episodes_aired") val episodesAired: Int,
        @field:SerializedName("aired_on") val dateAired: DateTime?,
        @field:SerializedName("next_episode_at") val nextEpisodeDate: DateTime?,
        @field:SerializedName("released_on") val dateReleased: DateTime?,
        @field:SerializedName("english") val namesEnglish: List<String?>?,
        @field:SerializedName("japanese") val namesJapanese: List<String?>?,
        @field:SerializedName("rating") val ageRating: AgeRating,
        @field:SerializedName("score") val score: Double,
        @field:SerializedName("duration") val duration: Int,
        @field:SerializedName("description") val description: String?,
        @field:SerializedName("description_html") val descriptionHtml: String,
        @field:SerializedName("favoured") val favoured: Boolean,
        @field:SerializedName("topic_id") val topicId: Long?,
        @field:SerializedName("genres") val genres: List<GenreResponse>,
        @field:SerializedName("user_rate") val userRate: UserRateResponse?,
        @field:SerializedName("videos") val videoResponses: List<AnimeVideoResponse>?,
        @field:SerializedName("studios") val studioResponses: List<StudioResponse>?,
        @field:SerializedName("rates_scores_stats") val rateScoresStats : List<StatisticResponse>,
        @field:SerializedName("rates_statuses_stats") val rateStatusesStats : List<StatisticResponse>
) {

    val status: Status
        get() = _status ?: Status.NONE

    val type: AnimeType
        get() = _type ?: AnimeType.NONE
}