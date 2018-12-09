package com.gnoemes.shikimori.entity.common.presentation

import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.studio.Studio

data class DetailsHeadItem(
        val detailsType: Type,
        val name: String,
        val nameSecond: String?,
        val image: Image,
        val type: String,
        val season: String,
        val status: String,
        val ageRating: String?,
        val score: Double,
        val genres: List<Genre>,
        val studio: Studio?
)