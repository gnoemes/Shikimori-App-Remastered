package com.gnoemes.shikimori.presentation.presenter.series.episodes

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.HttpStatusCode
import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.ServiceCodeException
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.series.domain.EpisodeChanges
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.entity.series.presentation.EpisodesNavigationData
import com.gnoemes.shikimori.entity.series.presentation.TranslationsNavigationData
import com.gnoemes.shikimori.presentation.presenter.anime.converter.EpisodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.series.episodes.EpisodesView
import com.gnoemes.shikimori.utils.clearAndAddAll
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class EpisodesPresenter @Inject constructor(
        private val interactor: SeriesInteractor,
        private val ratesInteractor: RatesInteractor,
        private val userInteractor: UserInteractor,
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
        subscribeToChanges()

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

    private fun showData(items: List<EpisodeViewModel>, isSearch: Boolean = false) {
        viewState.showLicencedError(false)
        viewState.showBlockedError(false)

        if (items.isNotEmpty()) {
            viewState.showData(items)
            viewState.hideEmptyView()
            viewState.showContent(true)
        } else {
            if (!isSearch) {
                viewState.showEmptyView()
                viewState.showContent(false)
            } else viewState.showSearchEmpty()
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
        val data = TranslationsNavigationData(navigationData.animeId, navigationData.image, navigationData.name, item.id, item.index, rateId, item.isFromAlternative)
        router.navigateTo(Screens.TRANSLATIONS, data)
    }

    fun onEpisodeLongClick(item: EpisodeViewModel) {
        viewState.showEpisodeOptionsDialog(item.index)
    }

    fun onEpisodeStatusChanged(item: EpisodeViewModel, newStatus: Boolean) {
        //TODO buffer to prevent multi loadEpisodes() requests
        Single.just(rateId)
                .flatMap { createRateIfNotExist(it) }
                .doOnSubscribe { showEpisodeLoading(item, newStatus) }
                .flatMapCompletable { rateId -> interactor.setEpisodeStatus(navigationData.animeId, item.index, rateId, newStatus) }
                .andThen(interactor.sendEpisodeChanges(EpisodeChanges(navigationData.animeId, item.index, newStatus)))
                .subscribe({}, this::processErrors)
                .addToDisposables()
    }

    private fun showEpisodeLoading(item: EpisodeViewModel, newStatus: Boolean) {
        items[items.indexOf(item)] = item.copy(isWatched = newStatus, state = EpisodeViewModel.State.Loading)
        viewState.showData(items)
    }

    private fun createRateIfNotExist(rateId: Long): Single<Long> {
        return when (rateId) {
            Constants.NO_ID -> ratesInteractor.createRateWithResult(navigationData.animeId, Type.ANIME, RateStatus.WATCHING).map { it.id!! }.doOnSuccess { this@EpisodesPresenter.rateId = it }
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
            showData(searchItems, true)
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
        if (isAlternativeSource) {
            super.processErrors(throwable)
            return
        }

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

    fun onCheckAllPrevious(index: Int) {
        Single.just(rateId)
                .flatMap { createRateIfNotExist(it) }
                .flatMap { userInteractor.getMyUserBrief() }
                .map { it.id }
                .map { UserRate(rateId, it, navigationData.animeId, Type.ANIME, episodes = index) }
                .flatMapCompletable { ratesInteractor.updateRate(it) }
                .andThen(Observable
                        .fromIterable(items.take(index))
                        .doOnNext { showEpisodeLoading(it, true) }
                        .toList()
                )
                .flatMap { loadEpisodes() }
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }

    private fun subscribeToChanges() {
        interactor.getEpisodeChanges()
                .buffer(interactor.getEpisodeChanges().debounce(Constants.BIG_DEBOUNCE_INTERVAL, TimeUnit.MILLISECONDS))
                .flatMapSingle { loadEpisodes() }
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }
}