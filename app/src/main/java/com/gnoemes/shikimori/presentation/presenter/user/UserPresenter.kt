package com.gnoemes.shikimori.presentation.presenter.user

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.main.BottomScreens
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.presentation.RateNavigationData
import com.gnoemes.shikimori.entity.user.domain.UserDetails
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import com.gnoemes.shikimori.entity.user.presentation.UserContentType
import com.gnoemes.shikimori.entity.user.presentation.UserProfileAction
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.user.converter.UserDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.view.user.UserView
import com.gnoemes.shikimori.utils.appendHostIfNeed
import com.gnoemes.shikimori.utils.appendLoadingLogic
import io.reactivex.Completable
import io.reactivex.Single
import java.net.URLEncoder
import javax.inject.Inject

@InjectViewState
class UserPresenter @Inject constructor(
        private val interactor: UserInteractor,
        private val converter: UserDetailsViewModelConverter,
        private val resourceProvider: CommonResourceProvider
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
                    .subscribe({ viewState.setFriends(currentUser.isMe, it) }, this::processErrors)
                    .addToDisposables()

    private fun loadClubs() =
            interactor.getClubs(id)
                    .map { converter.convertClubs(it) }
                    .subscribe({ viewState.setClubs(currentUser.isMe, it) }, this::processErrors)
                    .addToDisposables()

    fun onAction(action: UserProfileAction) {
        when (action) {
            is UserProfileAction.History -> onHistoryClicked()
            is UserProfileAction.Bans -> onBansClicked()
            is UserProfileAction.About -> onAboutClicked()
            is UserProfileAction.Message -> onMessageClicked()
            is UserProfileAction.More -> onMoreClicked(action.type)
            is UserProfileAction.ChangeIgnoreStatus -> onIgnoreStatusChanged(action.newStatus)
            is UserProfileAction.ChangeFriendshipStatus -> onFriendshipStatusChanged(action.newStatus)
            is UserProfileAction.RateClicked -> onRateClicked(action.isAnime, action.status)
        }
    }

    private fun onRateClicked(anime: Boolean, status: RateStatus) {
        val data = RateNavigationData(id, if (anime) Type.ANIME else Type.MANGA, status)
        router.navigateTo(BottomScreens.RATES, data)
    }

    private fun onFriendshipStatusChanged(newStatus: Boolean) {
        if (checkUserStatus()) {
            router.showSystemMessage(resourceProvider.needAuth)
            return
        }

        (if (newStatus) interactor.addToFriends(id)
        else interactor.removeFriend(id))
                .updateUserData()
    }

    private fun onIgnoreStatusChanged(newStatus: Boolean) {
        if (checkUserStatus()) return

        (if (newStatus) interactor.ignore(id)
        else interactor.unignore(id))
                .updateUserData()
    }

    private fun onMoreClicked(type: UserContentType) {
        when (type) {
            UserContentType.FRIENDS -> router.navigateTo(Screens.USER_FRIENDS, id)
            UserContentType.CLUBS -> router.navigateTo(Screens.USER_CLUBS, id)
            UserContentType.FAVORITES -> router.navigateTo(Screens.USER_FAVORITES, id)
        }
    }

    private fun onMessageClicked() {
        if (checkUserStatus()) return
        //TODO
    }

    private fun onAboutClicked() {
        onOpenWeb(URLEncoder.encode(currentUser.nickname, "utf-8").appendHostIfNeed())
    }

    private fun onBansClicked() {
        router.navigateTo(Screens.USER_BANS, id)
    }

    private fun onHistoryClicked() {
        router.navigateTo(Screens.USER_HISTORY, id)
    }

    private fun Completable.updateUserData() {
        andThen(loadUser(false))
                .subscribe({ }, this@UserPresenter::processErrors)
                .addToDisposables()
    }

    fun onRefresh() {
        loadData()
    }

    private fun checkUserStatus(): Boolean {
        return if (isGuest()) {
            router.showSystemMessage(resourceProvider.needAuth)
            true
        } else false
    }

    private fun isGuest(): Boolean = interactor.getUserStatus() == UserStatus.GUEST
}