package com.gnoemes.shikimori.entity.series.presentation

import com.gnoemes.shikimori.entity.common.domain.Image

open class BaseSeriesNavigationData(
        val animeId : Long,
        val image : Image,
        val name : String
)