package com.gnoemes.shikimori.entity.user.presentation

import com.gnoemes.shikimori.entity.user.domain.UserImage

class UserHeadViewModel(
        val name: String,
        val lastOnline: String,
        val image: UserImage
)