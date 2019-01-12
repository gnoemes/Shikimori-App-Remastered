package com.gnoemes.shikimori.domain.comment

import com.gnoemes.shikimori.data.repository.comments.CommentRepository
import com.gnoemes.shikimori.entity.comment.domain.Comment
import com.gnoemes.shikimori.entity.comment.domain.CommentableType
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class CommentInteractorImpl @Inject constructor(
        private val repository: CommentRepository
) : CommentInteractor {

    override fun getList(id: Long, type: CommentableType, page: Int, limit: Int, desc: Int): Single<List<Comment>> =
            repository.getList(id, type, page, limit, desc)
                    .applyErrorHandlerAndSchedulers()

    override fun getSingle(id: Long): Single<Comment> =
            repository.getSingle(id)
                    .applyErrorHandlerAndSchedulers()
}