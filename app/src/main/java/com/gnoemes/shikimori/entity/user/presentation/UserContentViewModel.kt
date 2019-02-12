package com.gnoemes.shikimori.entity.user.presentation

data class UserContentViewModel(
        val type: UserContentType,
        val content: List<UserContentItem>,
        val needShowMore: Boolean,
        val moreSize: Int
)