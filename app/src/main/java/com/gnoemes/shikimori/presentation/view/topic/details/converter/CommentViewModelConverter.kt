package com.gnoemes.shikimori.presentation.view.topic.details.converter

import com.gnoemes.shikimori.entity.comment.domain.Comment
import com.gnoemes.shikimori.entity.comment.presentation.CommentViewModel
import io.reactivex.functions.Function

interface CommentViewModelConverter : Function<List<Comment>, List<CommentViewModel>> {

    fun convertComment(it : Comment) : CommentViewModel
}