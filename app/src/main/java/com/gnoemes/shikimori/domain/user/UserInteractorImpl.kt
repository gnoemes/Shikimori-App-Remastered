package com.gnoemes.shikimori.domain.user

import com.gnoemes.shikimori.data.repository.user.UserRepository
import com.gnoemes.shikimori.entity.club.domain.Club
import com.gnoemes.shikimori.entity.user.domain.*
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserInteractorImpl @Inject constructor(
        private val repository: UserRepository
) : UserInteractor {

    override fun getMyUserId(): Single<Long> = repository.getMyUserId().applyErrorHandlerAndSchedulers()

    override fun getMyUserBrief(): Single<UserBrief> = repository.getMyUserBrief().applyErrorHandlerAndSchedulers()

    override fun getUserMessages(type: MessageType): Single<List<Message>> = repository.getUserMessages(type).applyErrorHandlerAndSchedulers()

    override fun getDetails(id: Long): Single<UserDetails> =
            repository.getDetails(id)
                    .flatMap { detals ->
                        repository.getMyUserId()
                                .map { detals.copy(isMe = detals.id == it) }
                                .onErrorReturn { detals.copy(isMe = false) }
                    }
                    .applyErrorHandlerAndSchedulers()

    override fun getFriends(id: Long): Single<List<UserBrief>> = repository.getFriends(id).applyErrorHandlerAndSchedulers()

    override fun getFavorites(id: Long): Single<FavoriteList> = repository.getFavorites(id).applyErrorHandlerAndSchedulers()

    override fun getClubs(id: Long): Single<List<Club>> = repository.getClubs(id).applyErrorHandlerAndSchedulers()

    override fun getHistory(id: Long, page: Int, limit: Int): Single<List<UserHistory>> = repository.getHistory(id, page, limit).applyErrorHandlerAndSchedulers()

    override fun ignore(id: Long): Completable = repository.ignore(id).applyErrorHandlerAndSchedulers()

    override fun unignore(id: Long): Completable = repository.unignore(id).applyErrorHandlerAndSchedulers()

    override fun addToFriends(id: Long): Completable = repository.addToFriends(id).applyErrorHandlerAndSchedulers()

    override fun removeFriend(id: Long): Completable = repository.removeFriend(id).applyErrorHandlerAndSchedulers()

    override fun getUserStatus(): UserStatus = repository.getUserStatus()

    override fun setUserStatus(status: UserStatus) = repository.setUserStatus(status)

    override fun clearUser() = repository.clearUser()
}