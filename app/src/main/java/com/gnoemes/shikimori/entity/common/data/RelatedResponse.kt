package com.gnoemes.shikimori.entity.common.data

import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.google.gson.annotations.SerializedName

data class RelatedResponse(
        @field:SerializedName("relation") val relation : String,
        @field:SerializedName("relation_russian") val relationRu : String?,
        @field:SerializedName("anime") val anime : AnimeResponse?,
        @field:SerializedName("manga") val manga : MangaResponse?
)