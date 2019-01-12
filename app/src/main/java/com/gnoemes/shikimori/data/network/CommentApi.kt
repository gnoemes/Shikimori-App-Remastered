package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.comment.data.CommentResponse
import com.gnoemes.shikimori.entity.comment.domain.CommentableType
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApi {

    @GET("/api/comments")
    fun getComments(@Query("commentable_id") id: Long,
                    @Query("commentable_type") type: CommentableType,
                    @Query("page") page: Int,
                    @Query("limit") limit: Int,
                    @Query("desc") desc: Int): Single<List<CommentResponse>>

    @GET("/api/comments/:id")
    fun getComment(@Path("id") id: Long): Single<CommentResponse>

}