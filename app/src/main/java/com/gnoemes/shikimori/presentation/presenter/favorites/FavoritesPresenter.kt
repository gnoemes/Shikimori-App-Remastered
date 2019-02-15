package com.gnoemes.shikimori.presentation.presenter.favorites

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.user.presentation.FavoriteViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.favorites.converter.FavoriteViewModelConverter
import com.gnoemes.shikimori.presentation.view.favorites.FavoritesView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class FavoritesPresenter @Inject constructor(
        private val interactor: UserInteractor,
        private val converter: FavoriteViewModelConverter
) : BaseNetworkPresenter<FavoritesView>() {

    var id: Long = Constants.NO_ID

    override fun initData() {
        loadData()
    }

    private fun loadData() =
            interactor.getFavorites(id)
                    .doOnSuccess { viewState.showFavoritesCount(it.all.size) }
                    .map(converter)
                    .appendLoadingLogic(viewState)
                    .subscribe(this::setData, this::processErrors)
                    .addToDisposables()

    private fun setData(items: List<FavoriteViewModel>) {
        viewState.showData(items)
    }
}