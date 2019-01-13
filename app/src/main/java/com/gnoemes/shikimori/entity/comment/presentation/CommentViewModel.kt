package com.gnoemes.shikimori.entity.comment.presentation

import com.gnoemes.shikimori.entity.comment.domain.CommentableType
import com.gnoemes.shikimori.entity.topic.presentation.TopicUserViewModel

data class CommentViewModel(
        val id: Long,
        val userId: Long,
        val commentableId: Long,
        val commentableType: CommentableType,
        val userData: TopicUserViewModel,
        val content: String?,
        val isOfftopic: Boolean,
        val isSummary: Boolean,
        val isEditable: Boolean
)