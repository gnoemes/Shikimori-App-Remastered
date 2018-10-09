package com.gnoemes.shikimori.entity.rates.data

import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.google.gson.annotations.SerializedName

data class RateResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("score") val score: Int,
        @field:SerializedName("status") val status: RateStatus,
        @field:SerializedName("text") val text: String?,
        @field:SerializedName("text_html") val textHtml: String?,
        @field:SerializedName("episodes") val episodes: Int?,
        @field:SerializedName("chapters") val chapters: Int?,
        @field:SerializedName("volumes") val volumes: Int?,
        @field:SerializedName("rewatches") val rewatches: Int?,
        @field:SerializedName("anime") val anime: AnimeResponse?,
        @field:SerializedName("manga") val manga: MangaResponse?
)