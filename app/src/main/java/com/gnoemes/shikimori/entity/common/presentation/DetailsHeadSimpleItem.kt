package com.gnoemes.shikimori.entity.common.presentation

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Type

data class DetailsHeadSimpleItem(
        val detailsType : Type,
        val image : Image,
        val firstLabel : String,
        val firstName : String?,
        val secondLabel : String,
        val secondName : String?,
        val othersLabel : String,
        val othersText : String?,
        val job : String?
)