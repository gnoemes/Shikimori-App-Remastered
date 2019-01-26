package com.gnoemes.shikimori.entity.series.presentation

import com.gnoemes.shikimori.entity.common.domain.Image

class TranslationsNavigationData(
        animeId: Long,
        image: Image,
        name: String,
        val episodeId: Long,
        val episodeIndex: Int,
        val rateId : Long,
        val isAlternative : Boolean
) : BaseSeriesNavigationData(animeId, image, name)