package com.gnoemes.shikimori.entity.anime.presentation

import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.studio.Studio

data class AnimeHeadItem(
        val name: String,
        val nameRu: String?,
        val image: Image,
        val type: String,
        val season: String,
        val status: String,
        val score: Double,
        val rateStatus: RateStatus?,
        val genres: List<Genre>,
        val studio: Studio?,
        val isGuest: Boolean
)