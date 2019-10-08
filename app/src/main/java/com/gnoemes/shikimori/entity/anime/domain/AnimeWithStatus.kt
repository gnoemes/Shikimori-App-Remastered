package com.gnoemes.shikimori.entity.anime.domain

import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class AnimeWithStatus(
        val anime: Anime,
        val rateId : Long? = null,
        val status: RateStatus? = null
)