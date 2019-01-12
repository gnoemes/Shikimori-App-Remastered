package com.gnoemes.shikimori.data.repository.comments

import com.gnoemes.shikimori.data.network.CommentApi
import com.gnoemes.shikimori.data.repository.comments.converter.CommentResponseConverter
import com.gnoemes.shikimori.entity.comment.domain.Comment
import com.gnoemes.shikimori.entity.comment.domain.CommentableType
import io.reactivex.Single
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
        private val api: CommentApi,
        private val converter: CommentResponseConverter
) : CommentRepository {

    override fun getList(id: Long, type: CommentableType, page: Int, limit: Int, desc: Int): Single<List<Comment>> =
            api.getComments(id, type, page, limit, desc)
                    .map(converter)
                    //server returns N+1 elements, if next page exists
                    .map { if (it.isNotEmpty()) it.take(limit) else it }

    override fun getSingle(id: Long): Single<Comment> =
            api.getComment(id)
                    .map{converter.convertResponse(it)}
}