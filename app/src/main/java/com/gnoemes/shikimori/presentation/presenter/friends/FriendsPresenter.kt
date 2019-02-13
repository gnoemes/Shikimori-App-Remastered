package com.gnoemes.shikimori.presentation.presenter.friends

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.user.presentation.FriendViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.friends.converter.FriendsViewModelConverter
import com.gnoemes.shikimori.presentation.view.friends.FriendsView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class FriendsPresenter @Inject constructor(
        private val interactor : UserInteractor,
        private val converter : FriendsViewModelConverter
) : BaseNetworkPresenter<FriendsView>(){

    var id : Long = Constants.NO_ID

    override fun initData() {
        loadData()
    }

    private fun loadData() =
            interactor.getFriends(id)
                    .appendLoadingLogic(viewState)
                    .map(converter)
                    .subscribe(this::showData, this::processErrors)
                    .addToDisposables()

    private fun showData(items : List<FriendViewModel>) {
        viewState.showFriendsCount(items.size)
        viewState.showData(items)
    }

    fun onRefresh() {
        loadData()
    }
}