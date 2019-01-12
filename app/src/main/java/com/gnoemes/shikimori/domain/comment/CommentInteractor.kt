package com.gnoemes.shikimori.domain.comment

import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.comment.domain.Comment
import com.gnoemes.shikimori.entity.comment.domain.CommentableType
import io.reactivex.Single

interface CommentInteractor {

    fun getList(id: Long, type: CommentableType, page: Int, limit: Int = Constants.DEFAULT_LIMIT, desc: Int = 1): Single<List<Comment>>

    fun getSingle(id: Long): Single<Comment>
}