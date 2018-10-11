package com.gnoemes.shikimori.entity.manga.domain

import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.roles.domain.Character
import org.joda.time.DateTime

data class MangaDetails(
        val id: Long,
        val name: String,
        val nameRu: String?,
        val image: Image,
        val url: String,
        val type: MangaType,
        val status: Status,
        val volumes: Int,
        val chapters: Int,
        val dateAired: DateTime?,
        val dateReleased: DateTime?,
        val score: Double,
        val description: String?,
        val descriptionHtml: String,
        val favoured: Boolean,
        val topicId: Long,
        val genres: List<Genre>,
        val characters: List<Character>,
        val userRate: UserRate?
)