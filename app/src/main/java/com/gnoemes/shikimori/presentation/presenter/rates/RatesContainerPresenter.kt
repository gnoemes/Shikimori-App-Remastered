package com.gnoemes.shikimori.presentation.presenter.rates

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.ContentException
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateCountConverter
import com.gnoemes.shikimori.presentation.view.rates.RatesContainerView
import com.gnoemes.shikimori.utils.appendLightLoadingLogic
import javax.inject.Inject

@InjectViewState
class RatesContainerPresenter @Inject constructor(
        private val interactor: UserInteractor,
        private val converter: RateCountConverter
) : BaseNetworkPresenter<RatesContainerView>() {

    var type: Type = Type.ANIME
    var userId: Long = Constants.NO_ID
    private val isAnime: Boolean
        get() = type == Type.ANIME

    override fun initData() {
        if (userId != Constants.NO_ID) loadRateCategories()
        else loadMyUser()
    }

    private fun loadMyUser() =
            interactor.getMyUserBrief()
                    .doOnSuccess { userId = it.id }
                    .doOnSuccess { loadRateCategories() }
                    .subscribe({ }, this::processUserErrors)

    private fun loadRateCategories() =
            interactor.getDetails(userId)
                    .appendLightLoadingLogic(viewState)
                    .map {
                        if (isAnime) converter.countAnimeRates(it)
                        else converter.countMangaRates(it)
                    }
                    .subscribe(this::setData, this::processErrors)
                    .addToDisposables()

    private fun setData(items: List<Pair<RateStatus, String>>) {
        viewState.setNavigationItems(items)
        if (items.isNotEmpty()) {
            onChangeStatus(items.first().first)
            viewState.hideEmptyView()
        } else {
            viewState.showEmptyView()
        }
    }

    private fun processUserErrors(throwable: Throwable) {
      when ((throwable as? BaseException)?.tag) {
          ContentException.TAG -> viewState.showEmptyView()
      }
    }

    fun onChangeType(type: Type) {
        this.type = type
        loadRateCategories()
    }

    fun onChangeStatus(rateStatus: RateStatus) {
        viewState.showStatusFragment(userId, type, rateStatus)
    }
}