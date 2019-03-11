package com.gnoemes.shikimori.presentation.presenter.series.episodes

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
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
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.series.episodes.converter.EpisodeViewModelConverter
import com.gnoemes.shikimori.presentation.view.series.episodes.EpisodesView
import com.gnoemes.shikimori.utils.clearAndAddAll
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class EpisodesPresenter @Inject constructor(
        private val interactor: SeriesInteractor,
        private val ratesInteractor: RatesInteractor,
        private val userInteractor: UserInteractor,
        private val converter: EpisodeViewModelConverter,
        private val settingsSource: SettingsSource,
        private val resourceProvider: CommonResourceProvider
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
        logEvent(AnalyticEvent.NAVIGATION_ANIME_TRANSLATIONS)
    }

    fun onEpisodeLongClick(item: EpisodeViewModel) {
        viewState.showEpisodeOptionsDialog(item.index)
    }

    fun onEpisodeStatusChanged(item: EpisodeViewModel, newStatus: Boolean) {
        if (userInteractor.getUserStatus() == UserStatus.GUEST) {
            router.showSystemMessage(resourceProvider.needAuthRates)
            return
        }

        (if (rateId == Constants.NO_ID) createRateIfNotExist(rateId).ignoreElement()
        else Completable.complete())
                .andThen(interactor.sendEpisodeChanges(EpisodeChanges(item.animeId, item.index, newStatus)))
                .doOnSubscribe { showEpisodeLoading(item, newStatus) }
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
        items.take(index).forEach { onEpisodeStatusChanged(it, true) }
    }

    private fun syncRate(changes: MutableList<EpisodeChanges>): Single<List<EpisodeViewModel>> =
            syncEpisodes(changes).andThen(loadEpisodes())

    private fun syncEpisodes(changes: List<EpisodeChanges>): Completable {
        return if (changes.size == 1) syncIterable(changes)
        else syncIterable(changes, true).andThen(syncPatch(items))
    }

    //uses items to get actual watched episodes (not changes)
    private fun syncPatch(changes: List<EpisodeViewModel>): Completable =
            Single.just(changes)
                    .map { UserRate(rateId, targetId = navigationData.animeId, targetType = Type.ANIME, episodes = it.count { episode -> episode.isWatched }) }
                    .flatMapCompletable { ratesInteractor.updateRate(it) }

    private fun syncIterable(changes: List<EpisodeChanges>, onlyLocal: Boolean = false): Completable =
            Observable.fromIterable(changes)
                    .flatMapCompletable { interactor.setEpisodeStatus(it.animeId, it.episodeIndex, rateId, it.isWatched, onlyLocal) }

    private fun subscribeToChanges() {
        interactor.getEpisodeChanges()
                .buffer(interactor.getEpisodeChanges().debounce(Constants.BIG_DEBOUNCE_INTERVAL, TimeUnit.MILLISECONDS))
                .flatMapSingle {
                    if (rateId == Constants.NO_ID && !settingsSource.isAutoStatus) loadEpisodes()
                    else syncRate(it)
                }
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }
}