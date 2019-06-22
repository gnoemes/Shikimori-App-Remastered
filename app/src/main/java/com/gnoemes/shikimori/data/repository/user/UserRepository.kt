package com.gnoemes.shikimori.data.repository.user

import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.club.domain.Club
import com.gnoemes.shikimori.entity.user.domain.*
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {

    fun getMyUserId() : Single<Long>

    fun getMyUserBrief(): Single<UserBrief>

    fun getUserMessages(type: MessageType): Single<List<Message>>

    fun getDetails(id: Long): Single<UserDetails>

    fun getFriends(id: Long): Single<List<UserBrief>>

    fun getFavorites(id: Long): Single<FavoriteList>

    fun getClubs(id: Long): Single<List<Club>>

    fun getHistory(id: Long, page: Int, limit: Int = Constants.DEFAULT_LIMIT): Single<List<UserHistory>>

    fun ignore(id: Long): Completable

    fun unignore(id: Long): Completable

    fun addToFriends(id: Long): Completable

    fun removeFriend(id: Long): Completable

    fun getUserStatus(): UserStatus

    fun setUserStatus(status: UserStatus)

    fun clearUser()
}