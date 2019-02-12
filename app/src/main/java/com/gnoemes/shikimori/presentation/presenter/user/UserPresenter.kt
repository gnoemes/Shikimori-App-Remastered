package com.gnoemes.shikimori.presentation.presenter.user

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.user.domain.UserDetails
import com.gnoemes.shikimori.entity.user.presentation.UserProfileAction
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.user.converter.UserDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.view.user.UserView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class UserPresenter @Inject constructor(
        private val interactor: UserInteractor,
        private val converter: UserDetailsViewModelConverter
) : BaseNetworkPresenter<UserView>() {

    var id: Long = Constants.NO_ID

    private lateinit var currentUser: UserDetails

    override fun initData() {
        loadData()
    }

    private fun loadData() =
            loadUser()
                    .doOnSuccess { loadFavorites() }
                    .doOnSuccess { loadFriends() }
                    .doOnSuccess { loadClubs() }
                    .subscribe({}, this::processErrors)
                    .addToDisposables()

    private fun loadUser(showLoading: Boolean = true): Single<UserDetails> =
            interactor.getDetails(id)
                    .flatMap {
                        if (showLoading) Single.just(it).appendLoadingLogic(viewState)
                        else Single.just(it)
                    }
                    .doOnSuccess { currentUser = it }
                    .doOnSuccess { viewState.setInfo(converter.convertInfo(it)) }
                    .doOnSuccess { viewState.setHead(converter.convertHead(it)) }
                    .doOnSuccess { viewState.setAnimeRate(converter.convertAnimeRate(it.stats.animeStatuses)) }
                    .doOnSuccess { viewState.setMangaRate(converter.convertMangaRate(it.stats.mangaStatuses)) }

    private fun loadFavorites() =
            interactor.getFavorites(id)
                    .map { converter.convertFavorites(it) }
                    .subscribe({ viewState.setFavorites(currentUser.isMe, it) }, this::processErrors)
                    .addToDisposables()

    private fun loadFriends() =
            interactor.getFriends(id)
                    .map { converter.convertFriends(it) }
                    .subscribe({viewState.setFriends(currentUser.isMe, it)}, this::processErrors)
                    .addToDisposables()

    private fun loadClubs() =
            interactor.getClubs(id)
                    .map { converter.convertClubs(it) }
                    .subscribe({viewState.setClubs(currentUser.isMe, it)}, this::processErrors)
                    .addToDisposables()

    fun onAction(action : UserProfileAction) {

    }

    private fun Completable.updateUserData() {
        andThen(loadUser(false))
                .subscribe({ }, this@UserPresenter::processErrors)
                .addToDisposables()
    }
}