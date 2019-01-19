package com.gnoemes.shikimori.entity.series.presentation

import com.gnoemes.shikimori.entity.common.domain.Image

class EpisodesNavigationData(
        animeId: Long,
        image: Image,
        name: String,
        val rateId: Long?
) : BaseSeriesNavigationData(animeId, image, name)