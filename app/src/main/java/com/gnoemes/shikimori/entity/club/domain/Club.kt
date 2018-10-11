package com.gnoemes.shikimori.entity.club.domain

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Type

data class Club(
        val id: Long,
        val name: String,
        val image: Image,
        val isCensored: Boolean,
        val policyJoin: ClubPolicy?,
        val policyComment: ClubPolicy?
) : LinkedContent(id, Type.CLUB, image.original, name)