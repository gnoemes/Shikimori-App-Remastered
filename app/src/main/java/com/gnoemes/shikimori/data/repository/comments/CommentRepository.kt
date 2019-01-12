package com.gnoemes.shikimori.data.repository.comments

import com.gnoemes.shikimori.entity.comment.domain.Comment
import com.gnoemes.shikimori.entity.comment.domain.CommentableType
import io.reactivex.Single

interface CommentRepository {

    fun getList(id : Long, type : CommentableType, page : Int, limit : Int, desc : Int) : Single<List<Comment>>

    fun getSingle(id : Long) : Single<Comment>
}