package com.gnoemes.shikimori.entity.rates.domain

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.manga.domain.Manga
import org.joda.time.DateTime

data class Rate(
        val id: Long,
        val score: Int,
        val status: RateStatus,
        val text: String?,
        val textHtml: String?,
        val episodes: Int?,
        val chapters: Int?,
        val volumes: Int?,
        val rewatches: Int?,
        val createdDateTime : DateTime?,
        val updatedDateTime : DateTime?,
        val anime: Anime?,
        val manga: Manga?,
        val type: Type = if (anime != null) Type.ANIME else if (manga?.isRanobe!!) Type.RANOBE else Type.MANGA
)