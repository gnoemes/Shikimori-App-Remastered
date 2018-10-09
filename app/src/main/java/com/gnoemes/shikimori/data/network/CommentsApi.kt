package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.topic.CommentResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CommentsApi {

    @GET("/api/comments")
    fun getList(@Query("commentable_id") id: Long,
                @Query("commentable_type") type: String,
                @Query("page") page: Int,
                @Query("limit") limit: Int): Single<List<CommentResponse>>
}