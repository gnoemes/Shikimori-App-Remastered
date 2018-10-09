package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.club.ClubResponse
import com.gnoemes.shikimori.entity.rates.data.RateResponse
import com.gnoemes.shikimori.entity.rates.data.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.entity.user.data.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface UserApi {

    @GET("/api/users/whoami")
    fun getCurrentUserBrief(): Single<UserBriefResponse>

    @GET("/api/users")
    fun getList(@Query("page") page: Int, @Query("limit") limit: Int): Single<List<UserBriefResponse>>

    @GET("/api/users/{id}/info")
    fun getUserBriefInfo(@Path("id") id: Long): Single<UserBriefResponse>

    @GET("/api/users/{id}/friends")
    fun getUserFriends(@Path("id") id: Long): Single<List<UserBriefResponse>>

    @GET("/api/users/{id}/anime_rates")
    fun getUserAnimeRates(@Path("id") id: Long,
                          @Query("page") page: Int,
                          @Query("limit") limit: Int,
                          @Query("status") status: String): Single<List<RateResponse>>

    @GET("/api/users/{id}/manga_rates")
    fun getUserMangaRates(@Path("id") id: Long,
                          @Query("page") page: Int,
                          @Query("limit") limit: Int,
                          @Query("status") status: String): Single<List<RateResponse>>

    @DELETE("/api/v2/user_rates/{id}")
    fun deleteRate(@Path("id") id: Long): Completable

    @POST("/api/v2/user_rates")
    fun createRate(@Body request: UserRateCreateOrUpdateRequest): Completable

    @PATCH("/api/v2/user_rates/{id}")
    fun updateRate(@Path("id") id: Long, @Body request: UserRateCreateOrUpdateRequest): Completable

    @POST("/api/v2/user_rates/{id}/increment")
    fun incrementEpisode(@Path("id") id: Long): Completable

    @GET("/api/users/{id}/favourites")
    fun getUserFavourites(@Path("id") id: Long): Single<FavoriteListResponse>

    @GET("/api/users/{id}/unread_messages")
    fun getUnreadMessages(@Path("id") id: Long): Single<UserUnreadMessagesCount>

    @GET("/api/users/{id}/messages")
    fun getUserMessages(@Path("id") id: Long, @Query("type") type: String, @Query("limit") limit: Int): Single<List<MessageResponse>>

    @GET("/api/users/{id}/history")
    fun getUserHistory(@Path("id") id: Long, @Query("page") page: Int, @Query("limit") limit: Int): Single<List<UserHistoryResponse>>

    @GET("/api/users/{id}/clubs")
    fun getUserClubs(@Path("id") id: Long): Single<List<ClubResponse>>

    @GET("/api/users/{id}")
    fun getUserProfile(@Path("id") id: Long): Single<UserProfileResponse>

    @POST("/api/friends/{id}")
    fun addToFriends(@Path("id") id: Long): Completable

    @DELETE("/api/friends/{id}")
    fun deleteFriend(@Path("id") id: Long): Completable

    @POST(" /api/v2/users/{user_id}/ignore")
    fun ignoreUser(@Path("user_id") id: Long): Completable

    @DELETE(" /api/v2/users/{user_id}/ignore")
    fun unignoreUser(@Path("user_id") id: Long): Completable
}