package com.gnoemes.shikimori.entity.topic.domain

import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import org.joda.time.DateTime

data class Topic(
        val id: Long,
        val title: String,
        val description: String?,
        val descriptionHtml: String?,
        val footer: String?,
        val dateCreated: DateTime,
        val commentsCount: Long,
        val forum: Forum,
        val user: UserBrief,
        val type: TopicType,
        val linkedType: Type,
        val linked: LinkedContent?,
        val isViewed: Boolean,
        val episode : String?
)