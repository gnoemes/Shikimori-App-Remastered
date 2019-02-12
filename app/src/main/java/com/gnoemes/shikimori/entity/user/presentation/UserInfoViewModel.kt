package com.gnoemes.shikimori.entity.user.presentation

data class UserInfoViewModel(
        val info : String,
        val isFriend : Boolean,
        val isIgnored : Boolean,
        val isMe : Boolean
)