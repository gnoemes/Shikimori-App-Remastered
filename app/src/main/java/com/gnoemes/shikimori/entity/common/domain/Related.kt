package com.gnoemes.shikimori.entity.common.domain

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.manga.domain.Manga

data class Related(
        val relation: String,
        val relationRu: String?,
        val anime: Anime?,
        val manga: Manga?,
        val type: Type = if (anime != null) Type.ANIME else Type.MANGA
)