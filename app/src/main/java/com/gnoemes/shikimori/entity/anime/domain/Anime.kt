package com.gnoemes.shikimori.entity.anime.domain

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import org.joda.time.DateTime

data class Anime(
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
        val dateReleased: DateTime?
) : LinkedContent(id, Type.ANIME, image.original, name)