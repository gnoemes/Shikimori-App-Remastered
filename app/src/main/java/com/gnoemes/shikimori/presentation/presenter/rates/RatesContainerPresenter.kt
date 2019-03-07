package com.gnoemes.shikimori.presentation.presenter.rates

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.ContentException
import com.gnoemes.shikimori.entity.app.domain.exceptions.NetworkException
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.presentation.RateCategory
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
    var priorityStatus : RateStatus? = null

    private val isAnime: Boolean
        get() = type == Type.ANIME

    override fun initData() {
        viewState.setNavigationItems(emptyList())

        if (userId != Constants.NO_ID) viewState.selectType(type)

        loadData()
    }

    override fun onViewReattached() {
        super.onViewReattached()
        loadData()
    }

    private fun loadData() {
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

    private fun setData(items: List<RateCategory>) {
        viewState.setNavigationItems(items)
        if (items.isNotEmpty()) {
            onChangeStatus(priorityStatus ?: items.first().status)
            viewState.hideEmptyView()
            viewState.hideNetworkView()
            viewState.showContainer()
            priorityStatus = null
        } else {
            viewState.showEmptyView()
            viewState.hideNetworkView()
            viewState.hideContainer()
        }
    }

    private fun processUserErrors(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            ContentException.TAG -> {
                viewState.showEmptyView()
                viewState.hideContainer()
            }
        }
    }

    override fun processErrors(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            NetworkException.TAG -> {
                viewState.showNetworkView()
                viewState.hideContainer()
            }
            else -> super.processErrors(throwable)
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