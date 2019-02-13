package com.gnoemes.shikimori.presentation.presenter.clubs

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.entity.club.presentation.UserClubViewModel
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.clubs.converter.UserClubViewModelConverter
import com.gnoemes.shikimori.presentation.view.clubs.UserClubsView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class UserClubsPresenter @Inject constructor(
        private val interactor: UserInteractor,
        private val converter : UserClubViewModelConverter

) : BaseNetworkPresenter<UserClubsView>() {

    var id : Long = Constants.NO_ID

    override fun initData() {
        loadData()
    }

    private fun loadData() =
            interactor.getClubs(id)
                    .appendLoadingLogic(viewState)
                    .map(converter)
                    .subscribe(this::showData, this::processErrors)
                    .addToDisposables()

    private fun showData(items : List<UserClubViewModel>) {
        viewState.showClubsCount(items.size)
        viewState.showData(items)
    }

    fun onRefresh() {
        loadData()
    }
}