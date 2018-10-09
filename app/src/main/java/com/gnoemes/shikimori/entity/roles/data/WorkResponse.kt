package com.gnoemes.shikimori.entity.roles.data

import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.google.gson.annotations.SerializedName

data class WorkResponse(
        @field:SerializedName("anime") val anime : AnimeResponse?,
        @field:SerializedName("manga") val manga : MangaResponse?,
        @field:SerializedName("role") val role : String
)