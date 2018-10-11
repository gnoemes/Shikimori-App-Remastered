package com.gnoemes.shikimori.data.repository.user

import com.gnoemes.shikimori.data.local.preference.UserSource
import com.gnoemes.shikimori.data.network.UserApi
import com.gnoemes.shikimori.data.repository.club.ClubResponseConverter
import com.gnoemes.shikimori.data.repository.user.converter.*
import com.gnoemes.shikimori.entity.club.domain.Club
import com.gnoemes.shikimori.entity.user.domain.*
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
        private val api: UserApi,
        private val userSource: UserSource,
        private val converter: UserBriefResponseConverter,
        private val detailsConverter: UserDetailsResponseConverter,
        private val historyConverter: UserHistoryConverter,
        private val favoriteConverter: FavoriteListResponseConverter,
        private val clubConverter: ClubResponseConverter,
        private val messageConverter: MessageResponseConverter
) : UserRepository {

    override fun getMyUserBrief(): Single<UserBrief> =
            Single.fromCallable { userSource.getUser()!! }
                    .onErrorResumeNext { _ ->
                        api.getCurrentUserBrief()
                                .map { converter.convertResponse(it)!! }
                                .doOnSuccess { userSource.setUser(it) }
                    }


    override fun getUserMessages(type: MessageType): Single<List<Message>> =
            getMyUserBrief()
                    .flatMap { api.getUserMessages(it.id, type) }
                    .map(messageConverter)

    override fun getDetails(id: Long): Single<UserDetails> =
            api.getUserProfile(id)
                    .map(detailsConverter::convertResponse)

    override fun getFriends(id: Long): Single<List<UserBrief>> =
            api.getUserFriends(id)
                    .map(converter)

    override fun getFavorites(id: Long): Single<FavoriteList> =
            api.getUserFavourites(id)
                    .map(favoriteConverter)

    override fun getClubs(id: Long): Single<List<Club>> =
            api.getUserClubs(id)
                    .map(clubConverter)

    override fun getHistory(id: Long, page: Int, limit: Int): Single<List<UserHistory>> =
            api.getUserHistory(id, page, limit)
                    .map(historyConverter)

    override fun ignore(id: Long): Completable = api.ignoreUser(id)

    override fun unignore(id: Long): Completable = api.unignoreUser(id)

    override fun addToFriends(id: Long): Completable = api.addToFriends(id)

    override fun removeFriend(id: Long): Completable = api.deleteFriend(id)

    override fun getUserStatus(): UserStatus = userSource.getUserStatus()

    override fun setUserStatus(status: UserStatus) = userSource.setUserStatus(status)

    override fun clearUser() = userSource.clearUser()
}