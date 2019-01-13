package com.gnoemes.shikimori.entity.topic.presentation

import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.topic.domain.TopicEvent
import com.gnoemes.shikimori.entity.topic.domain.TopicType

data class TopicViewModel(
        val id: Long,
        val userData: TopicUserViewModel,
        val contentData: TopicContentViewModel,
        val commentsCount : Long,
        val type: TopicType,
        val linked: LinkedContent?,
        val isViewed: Boolean,
        val event : TopicEvent,
        val episode: String?,
        val hasTag: Boolean = userData.tag != null
)