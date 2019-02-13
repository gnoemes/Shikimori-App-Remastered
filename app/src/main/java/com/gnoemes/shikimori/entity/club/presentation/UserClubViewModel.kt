package com.gnoemes.shikimori.entity.club.presentation

import com.gnoemes.shikimori.entity.common.domain.Image

data class UserClubViewModel(
        val id : Long,
        val image : Image,
        val name : String,
        val description : String,
        val isCensored : Boolean
)