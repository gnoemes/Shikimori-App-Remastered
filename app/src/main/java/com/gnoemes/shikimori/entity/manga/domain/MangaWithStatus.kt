package com.gnoemes.shikimori.entity.manga.domain

import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class MangaWithStatus(
        val manga : Manga,
        val rateId : Long? = null,
        val status : RateStatus? = null
)