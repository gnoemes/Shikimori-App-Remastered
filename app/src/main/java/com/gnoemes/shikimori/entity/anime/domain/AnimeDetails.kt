package com.gnoemes.shikimori.entity.anime.domain

import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.roles.domain.Character
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
        val namesEnglish: List<String?>?,
        val namesJapanese: List<String?>?,
        val score: Double,
        val duration: Int,
        val description: String?,
        val descriptionHtml: String,
        val favoured: Boolean,
        val topicId: Long,
        val genres: List<Genre>,
        val userRate: UserRate?,
        val videoResponses: List<AnimeVideo>?,
        val charactes: List<Character>
)