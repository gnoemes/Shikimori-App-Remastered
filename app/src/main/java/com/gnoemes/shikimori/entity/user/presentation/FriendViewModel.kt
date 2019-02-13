package com.gnoemes.shikimori.entity.user.presentation

import com.gnoemes.shikimori.entity.user.domain.UserImage

data class FriendViewModel(
        val id: Long,
        val image: UserImage,
        val name: String,
        val lastOnline: String
)