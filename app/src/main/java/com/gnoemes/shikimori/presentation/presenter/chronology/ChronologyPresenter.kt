package com.gnoemes.shikimori.presentation.presenter.chronology

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.BuildConfig
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.chronology.ChronologyInteractor
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.chronology.ChronologyNavigationData
import com.gnoemes.shikimori.entity.chronology.ChronologyType
import com.gnoemes.shikimori.entity.chronology.ChronologyViewModel
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.chronology.converter.ChronologyViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.view.base.activity.BaseView
import com.gnoemes.shikimori.presentation.view.chronology.ChronologyView
import com.gnoemes.shikimori.utils.clearAndAddAll
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class ChronologyPresenter @Inject constructor(
        private val interactor: ChronologyInteractor,
        private val ratesInteractor: RatesInteractor,
        private val userInteractor: UserInteractor,
        private val converter: ChronologyViewModelConverter,
        private val settings: SettingsSource,
        private val resourceProvider: CommonResourceProvider
) : BaseNetworkPresenter<ChronologyView>() {

    lateinit var data: ChronologyNavigationData

    private val items = mutableListOf<ChronologyViewModel>()

    private var chronologyType = ChronologyType.LINKED_DIRECTLY

    private var userId: Long? = null
    private var selectedItem: ChronologyViewModel? = null

    override fun initData() {
        chronologyType = settings.chronologyType

        loadData()
        loadUser()
    }

    private fun loadUser() =
            userInteractor.getMyUserId()
                    .subscribe({ userId = it }, this::processUserErrors)
                    .addToDisposables()

    private fun loadData() {
        (when (data.type) {
            Type.ANIME -> interactor.getAnimes(data.id, data.franchise, chronologyType)
            Type.MANGA -> interactor.getMangas(data.id, data.franchise, chronologyType)
            Type.RANOBE -> interactor.getRanobes(data.id, data.franchise, chronologyType)
            else -> throw IllegalArgumentException("${data.type} is not supporting")
        }).appendLoadingLogic(viewState)
                .map { converter.apply(it, userId == null) }
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }

    private fun setData(it: List<ChronologyViewModel>) {
        val scrollPos = if (items.isEmpty()) it.indexOfFirst { item -> item.id == data.id } else 0

        items.clearAndAddAll(it)
        if (it.isEmpty()) viewState.showEmptyView()
        else {
            viewState.showData(it)
            viewState.scrollTo(scrollPos)
        }
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

    private fun createRate(item: ChronologyViewModel, newStatus: RateStatus) {
        if (userId != Constants.NO_ID) {
            ratesInteractor.createRateWithResult(item.id, item.type, newStatus)
                    .ignoreElement()
                    .subscribe(this::onRefresh, this::processErrors)
                    .addToDisposables()
        } else {
            router.showSystemMessage(resourceProvider.needAuth)
        }
    }

    fun onFabClicked() {
        viewState.showTypeDialog(chronologyType)
    }

    fun onTypeChanged(newType: ChronologyType) {
        chronologyType = newType
        settings.chronologyType = newType
        loadData()
    }

    fun onWebClicked() {
        onOpenWeb(getUrl())
    }

    fun onShareClicked() {
        router.navigateTo(Screens.SHARE, getUrl())
    }

    private fun processUserErrors(it: Throwable) {
        it.printStackTrace()
    }

    fun onRefresh() {
        loadData()
    }

    private fun getUrl(): String? {
        val path = (when (data.type) {
            Type.ANIME -> "animes"
            Type.MANGA -> "mangas"
            Type.RANOBE -> "ranobes"
            else -> return null
        }).plus("/${data.id}/franchise")
        return BuildConfig.ShikimoriBaseUrl + path
    }

    private fun <T> Single<T>.appendLoadingLogic(viewState: BaseView): Single<T> =
            this.doOnSubscribe { viewState.onShowLoading() }
                    .doOnSubscribe { viewState.hideEmptyView() }
                    .doOnSubscribe { viewState.hideNetworkView() }
                    .doAfterTerminate { viewState.onHideLoading() }
                    .doOnEvent { _, _ -> viewState.onHideLoading() }
                    .doOnSuccess { viewState.showContent(true) }
}