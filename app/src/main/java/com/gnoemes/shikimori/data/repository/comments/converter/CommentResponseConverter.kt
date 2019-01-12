package com.gnoemes.shikimori.data.repository.comments.converter

import com.gnoemes.shikimori.entity.comment.data.CommentResponse
import com.gnoemes.shikimori.entity.comment.domain.Comment
import io.reactivex.functions.Function

interface CommentResponseConverter : Function<List<CommentResponse>, List<Comment>> {
    fun convertResponse(it: CommentResponse): Comment
}