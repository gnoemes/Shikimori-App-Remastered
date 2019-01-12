package com.gnoemes.shikimori.entity.topic.presentation

import androidx.annotation.ColorRes
import com.gnoemes.shikimori.entity.user.domain.UserBrief

data class TopicUserViewModel(
        val user: UserBrief,
        val createdDate: String,
        val tag: String?,
        @ColorRes val tagColor: Int
)