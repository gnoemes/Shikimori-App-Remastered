package com.gnoemes.shikimori.entity.comment.domain

import com.gnoemes.shikimori.entity.user.domain.UserBrief
import org.joda.time.DateTime

data class Comment(
        val id: Long,
        val userId: Long,
        val commentableId: Long,
        val commentableType: CommentableType,
        val body: String?,
        val bodyHtml: String?,
        val dateCreated: DateTime,
        val dateUpdated: DateTime,
        val isOfftopic: Boolean,
        val isSummary: Boolean,
        val isEditable: Boolean,
        val user: UserBrief
)