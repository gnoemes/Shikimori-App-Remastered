package com.gnoemes.shikimori.data.repository.comments.converter

import com.gnoemes.shikimori.data.repository.user.converter.UserBriefResponseConverter
import com.gnoemes.shikimori.entity.comment.data.CommentResponse
import com.gnoemes.shikimori.entity.comment.domain.Comment
import javax.inject.Inject

class CommentResponseConverterImpl @Inject constructor(
        private val userConverter: UserBriefResponseConverter
) : CommentResponseConverter {

    override fun apply(t: List<CommentResponse>): List<Comment> =
            t.map { convertResponse(it) }

    override fun convertResponse(it: CommentResponse): Comment = Comment(
            it.id,
            it.userId,
            it.commentableId,
            it.commentableType,
            it.body,
            it.bodyHtml,
            it.dateCreated,
            it.dateUpdated,
            it.isOfftopic,
            it.isSummary,
            it.isEditable,
            userConverter.convertResponse(it.user)!!
    )

}