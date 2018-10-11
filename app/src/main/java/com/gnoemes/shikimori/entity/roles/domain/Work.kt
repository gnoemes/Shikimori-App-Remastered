package com.gnoemes.shikimori.entity.roles.domain

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.manga.domain.Manga

data class Work(
        val anime: Anime?,
        val manga: Manga?,
        val role: String,
        val type: Type = if (anime != null) Type.ANIME else Type.MANGA
)