package com.gnoemes.shikimori.presentation.presenter.similar

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.similar.SimilarInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.CommonNavigationData
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.similar.presentation.SimilarViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.similar.converter.SimilarViewModelConverter
import com.gnoemes.shikimori.presentation.view.base.activity.BaseView
import com.gnoemes.shikimori.presentation.view.similar.SimilarView
import com.gnoemes.shikimori.utils.clearAndAddAll
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class SimilarPresenter @Inject constructor(
        private val interactor: SimilarInteractor,
        private val ratesInteractor: RatesInteractor,
        private val userInteractor: UserInteractor,
        private val converter: SimilarViewModelConverter,
        private val resourceProvider: CommonResourceProvider
) : BaseNetworkPresenter<SimilarView>() {

    lateinit var navigationData: CommonNavigationData

    private val items = mutableListOf<SimilarViewModel>()

    private var userId: Long? = null
    private var selectedItem: SimilarViewModel? = null

    override fun initData() {
        loadData()
        loadUser()
    }

    private fun loadUser() =
            userInteractor.getMyUserId()
                    .subscribe({ userId = it }, this::processUserErrors)
                    .addToDisposables()

    private fun loadData() {
        (when (navigationData.type) {
            Type.ANIME -> interactor.getAnimes(navigationData.id).map { converter.apply(it, userId == null) }
            Type.MANGA -> interactor.getMangas(navigationData.id).map { converter.apply(it, userId == null) }
            Type.RANOBE -> interactor.getRanobes(navigationData.id).map { converter.apply(it, userId == null) }
            else -> throw IllegalArgumentException("${navigationData.type} is not supporting")
        }).appendLoadingLogic(viewState)
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }

    private fun setData(it: List<SimilarViewModel>) {
        items.clearAndAddAll(it)
        if (it.isEmpty()) viewState.showEmptyView()
        else viewState.showData(it)
    }

    fun onShowStatusDialog(id: Long) {
        val item = items.find { it.id == id }
        if (item != null) {
            selectedItem = item
            viewState.showStatusDialog(item.rateId, item.title, item.status, item.type == Type.ANIME)
        }
    }

    fun onChangeRateStatus(id: Long, newStatus: RateStatus) {
        if (selectedItem != null) when (id) {
            Constants.NO_ID -> createRate(selectedItem!!, newStatus)
            else -> changeRate(id, newStatus)
        }
    }

    private fun changeRate(id: Long, newStatus: RateStatus) {
        ratesInteractor.changeRateStatus(id, newStatus)
                .subscribe(this::onRefresh, this::processErrors)
                .addToDisposables()
        logEvent(AnalyticEvent.RATE_DROP_MENU)
    }

    private fun createRate(item: SimilarViewModel, newStatus: RateStatus) {
        if (userId != Constants.NO_ID) {
            ratesInteractor.createRateWithResult(item.id, item.type, newStatus)
                    .ignoreElement()
                    .subscribe(this::onRefresh, this::processErrors)
                    .addToDisposables()
        } else {
            router.showSystemMessage(resourceProvider.needAuth)
        }
    }

    private fun processUserErrors(it: Throwable) {
        it.printStackTrace()
    }

    fun onRefresh() {
        loadData()
    }

    private fun <T> Single<T>.appendLoadingLogic(viewState: BaseView): Single<T> =
            this.doOnSubscribe { viewState.onShowLoading() }
                    .doOnSubscribe { viewState.hideEmptyView() }
                    .doOnSubscribe { viewState.hideNetworkView() }
                    .doAfterTerminate { viewState.onHideLoading() }
                    .doOnEvent { _, _ -> viewState.onHideLoading() }
                    .doOnSuccess { viewState.showContent(true) }
}