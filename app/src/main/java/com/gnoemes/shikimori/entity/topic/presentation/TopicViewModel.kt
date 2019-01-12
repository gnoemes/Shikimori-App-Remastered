package com.gnoemes.shikimori.entity.topic.presentation

import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.topic.domain.TopicEvent
import com.gnoemes.shikimori.entity.topic.domain.TopicType
import com.gnoemes.shikimori.entity.user.domain.UserBrief

data class TopicViewModel(
        val id: Long,
        val user: UserBrief,
        val createdDate: String,
        val tag : String?,
        val title: String?,
        val body: String?,
        val bodyHtml: String?,
        val footerHtml : String?,
        val commentsCount : Int,
        val type: TopicType,
        val linked: LinkedContent?,
        val isViewed: Boolean,
        val event : TopicEvent,
        val episode: String?,
        val hasTag: Boolean = tag != null
)