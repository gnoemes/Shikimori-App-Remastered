package com.gnoemes.shikimori.entity.anime.domain

import com.gnoemes.shikimori.entity.common.domain.AgeRating
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.studio.Studio
import com.gnoemes.shikimori.entity.user.domain.Statistic
import org.joda.time.DateTime

data class AnimeDetails(
        val id: Long,
        val name: String,
        val nameRu: String?,
        val image: Image,
        val url: String,
        val type: AnimeType,
        val status: Status,
        val episodes: Int,
        val episodesAired: Int,
        val dateAired: DateTime?,
        val dateReleased: DateTime?,
        val nextEpisodeDate: DateTime?,
        val namesEnglish: List<String?>?,
        val namesJapanese: List<String?>?,
        val ageRating: AgeRating,
        val score: Double,
        val duration: Int,
        val description: String?,
        val descriptionHtml: String,
        val franchise : String?,
        val favoured: Boolean,
        val topicId: Long?,
        val genres: List<Genre>,
        val userRate: UserRate?,
        val videos: List<AnimeVideo>?,
        val studios: List<Studio>,
        val rateScoresStats: List<Statistic>,
        val rateStatusesStats: List<Statistic>
)