package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.forum.data.ForumResponse
import com.gnoemes.shikimori.entity.topic.data.TopicResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TopicApi {

    @GET("/api/topics")
    fun getList(@Query("page") page: Int,
                @Query("limit") limit: Int,
                @Query("forum") forum: String,
                @Query("linked_type") linkedType: String? = null,
                @Query("linked_id") linkedId: Long? = null
    ): Single<List<TopicResponse>>

    @GET("/api/topics/{id}")
    fun getDetails(@Path("id") id: Long): Single<TopicResponse>

    @GET("/api/forums")
    fun getForums(): Single<List<ForumResponse>>

    @GET("/api/animes/{id}/topics")
    fun getAnimeEpisodeTopic(
            @Path("id") id: Long,
            @Query("episode") episode: Int
    ): Single<List<TopicResponse>>

}