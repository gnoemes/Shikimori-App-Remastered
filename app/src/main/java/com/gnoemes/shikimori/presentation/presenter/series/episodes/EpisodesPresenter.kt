package com.gnoemes.shikimori.presentation.presenter.series.episodes

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.HttpStatusCode
import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.ServiceCodeException
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.entity.series.presentation.EpisodesNavigationData
import com.gnoemes.shikimori.presentation.presenter.anime.converter.EpisodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.series.episodes.EpisodesView
import com.gnoemes.shikimori.utils.clearAndAddAll
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class EpisodesPresenter @Inject constructor(
        private val interactor: SeriesInteractor,
        private val ratesInteractor: RatesInteractor,
        private val converter: EpisodeViewModelConverter
) : BaseNetworkPresenter<EpisodesView>() {

    lateinit var navigationData: EpisodesNavigationData
    private var rateId = Constants.NO_ID
    private var isAlternativeSource = false

    private val items = mutableListOf<EpisodeViewModel>()

    override fun initData() {
        super.initData()
        loadData()
        rateId = navigationData.rateId ?: rateId

        viewState.setBackground(navigationData.image)
        viewState.setName(navigationData.name)
    }

    fun onRefresh() {
        loadData()
    }

    private fun loadData() =
            loadEpisodes()
                    .doOnSubscribe { viewState.onShowLoading() }
                    .doOnSubscribe { viewState.hideEmptyView() }
                    .doOnSubscribe { viewState.hideNetworkView() }
                    .doOnSubscribe { viewState.showBlockedError(false) }
                    .doOnSubscribe { viewState.showLicencedError(false) }
                    .doOnSubscribe { viewState.showContent(true) }
                    .doAfterTerminate { viewState.onHideLoading() }
                    .doOnEvent { _, _ -> viewState.onHideLoading() }
                    .subscribe(this::setData, this::processErrors)
                    .addToDisposables()

    private fun loadEpisodes(): Single<List<EpisodeViewModel>> =
            interactor.getEpisodes(navigationData.animeId, isAlternativeSource)
                    .map(converter)

    private fun setData(items: List<EpisodeViewModel>) {
        val first = this.items.isEmpty()
        this.items.clearAndAddAll(items)
        showData(items)

        if (first) scrollToPenultimate()
    }

    private fun showData(items: List<EpisodeViewModel>) {
        if (items.isNotEmpty()) {
            viewState.showData(items)
            viewState.hideEmptyView()
            viewState.showContent(true)
        } else {
            viewState.showEmptyView()
            viewState.showContent(false)
        }
    }

    private fun scrollToPenultimate() {
        var lastWatchedWithIgnoredGaps = 0
        kotlin.run loop@{
            items.forEachIndexed { index, episodeViewModel ->
                if (episodeViewModel.isWatched) lastWatchedWithIgnoredGaps = index
                if (!episodeViewModel.isWatched && lastWatchedWithIgnoredGaps != 0) return@loop
            }
        }
        viewState.scrollToPosition(lastWatchedWithIgnoredGaps - 1)
    }

    fun onEpisodeClicked(item: EpisodeViewModel) {
        //TODO
        router.navigateTo(Screens.TRANSLATIONS)
    }

    fun onEpisodeStatusChanged(item: EpisodeViewModel, newStatus: Boolean) {
        //TODO buffer to prevent multi loadEpisodes() requests
        Single.just(rateId)
                .flatMap { createRateIfNotExist(it) }
                .doOnSubscribe { showEpisodeLoading(item, newStatus) }
                .flatMapCompletable { rateId -> interactor.setEpisodeStatus(navigationData.animeId, item.index, rateId, newStatus) }
                .andThen(loadEpisodes())
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }

    private fun showEpisodeLoading(item: EpisodeViewModel, newStatus: Boolean) {
        items[items.indexOf(item)] = item.copy(isWatched = newStatus, state = EpisodeViewModel.State.Loading)
        viewState.showData(items)
    }

    private fun createRateIfNotExist(rateId: Long): Single<Long> {
        return when (rateId) {
            Constants.NO_ID -> ratesInteractor.createRateWithResult(navigationData.animeId, Type.ANIME, RateStatus.WATCHING).map { it.id }
            else -> Single.just(rateId)
        }
    }

    fun onSearchClicked() {
        viewState.showSearchView()
    }

    fun onSearchClosed() {
        //restoring first state
        showData(items)
        scrollToPenultimate()
    }

    fun onAlternativeSourceClicked() {
        isAlternativeSource = !isAlternativeSource
        items.clear()
        viewState.showAlternativeLabel(isAlternativeSource)
        onRefresh()
    }

    fun onQueryChanged(newText: String?) {
        val text = newText ?: ""

        if (text.isBlank()) {
            showData(items)
        } else {
            val searchItems = items.filter { it.index.toString().contains(text) }
            showData(searchItems)
        }

        viewState.scrollToPosition(0)
    }

    override fun processErrors(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            ServiceCodeException.TAG -> processServiceCodeException(throwable as ServiceCodeException)
            else -> super.processErrors(throwable)
        }
    }

    private fun processServiceCodeException(throwable: ServiceCodeException) {
        viewState.apply {
            when (throwable.serviceCode) {
                HttpStatusCode.FORBIDDED -> showLicencedError(true)
                HttpStatusCode.NOT_FOUND -> showBlockedError(true)
                else -> super.processErrors(throwable)
            }

            showContent(false)
            router.showSystemMessage("HTTP error ${throwable.serviceCode}")
        }
    }
}