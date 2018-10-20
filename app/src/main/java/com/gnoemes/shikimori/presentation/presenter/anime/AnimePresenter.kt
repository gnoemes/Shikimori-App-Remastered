package com.gnoemes.shikimori.presentation.presenter.anime

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.anime.AnimeInteractor
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.related.RelatedInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.NetworkException
import com.gnoemes.shikimori.entity.app.domain.exceptions.ServiceCodeException
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.PlaceholderItem
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.presenter.anime.converter.AnimeDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.anime.converter.EpisodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.anime.AnimeView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class AnimePresenter @Inject constructor(
        private val animeInteractor: AnimeInteractor,
        private val seriesInteractor: SeriesInteractor,
        private val ratesInteractor: RatesInteractor,
        private val userInteractor: UserInteractor,
        private val relatedInteractor: RelatedInteractor,
        private val viewModelConverter: AnimeDetailsViewModelConverter,
        private val episodeConverter: EpisodeViewModelConverter
) : BaseNetworkPresenter<AnimeView>() {

    var id: Long = Constants.NO_ID
    private var rateId: Long = Constants.NO_ID
    private var userId: Long = Constants.NO_ID

    private lateinit var currentAnime: AnimeDetails

    override fun initData() {
        loadData()
        loadUser()
    }

    private fun loadData() =
            loadAnime()
                    .doOnSuccess { loadEpisodes() }
                    .doOnSuccess { loadSimilar() }
                    .doOnSuccess { loadRelated() }
                    .subscribe({ viewState.setAnime(it) }, this::processErrors)

    private fun loadEpisodes() =
            seriesInteractor.getSeries(id)
                    .doOnSubscribe { viewState.showEpisodeLoading() }
                    .doAfterTerminate { viewState.hideEpisodeLoading() }
                    .doOnEvent { _, _ -> viewState.hideEpisodeLoading() }
                    .map(episodeConverter)
                    .subscribe({ viewState.setEpisodes(it) }, this::processEpisodeErrors)

    private fun loadUser() =
            userInteractor.getMyUserBrief()
                    .subscribe({ userId = it.id }, this::processErrors)

    private fun loadAnime() =
            animeInteractor.getDetails(id)
                    .appendLoadingLogic(viewState)
                    .doOnSuccess { currentAnime = it; rateId = it.userRate?.id ?: Constants.NO_ID }
                    .map(viewModelConverter)

    private fun loadSimilar() =
            animeInteractor.getSimilar(id)
                    .map { viewModelConverter.convertSimilar(it) }
                    .subscribe({ viewState.updateSimilar(it) }, this::processErrors)

    private fun loadRelated() =
            relatedInteractor.getAnime(id)
                    .map { viewModelConverter.convertRelated(it) }
                    .subscribe({ viewState.updateRelated(it) }, this::processErrors)

    private fun loadLinks() {

    }

    private fun loadChronology() {

    }

    fun onAction(action: DetailsAction) {
        when (action) {
            is DetailsAction.Links -> loadLinks()
            is DetailsAction.Chronology -> loadChronology()
            is DetailsAction.WatchOnline -> onWatchOnline()
            is DetailsAction.EditRate -> onEditRate()
            is DetailsAction.OpenInBrowser -> onOpenInBrowser()
            is DetailsAction.ClearHistory -> onClearHistory()
            is DetailsAction.Video -> onOpenWeb(action.url)
            is DetailsAction.GenreClicked -> onGenreClicked(action.genre)
            is DetailsAction.ChangeRateStatus -> onChangeRateStatus(action.newStatus)
            is DetailsAction.Screenshots -> onScreenshotsClicked()
            is DetailsAction.Discussion -> onOpenDiscussion()
            is DetailsAction.StudioClicked -> onStudioClicked(action.id)
        }
    }

    private fun onStudioClicked(id: Long) {

    }

    private fun onOpenDiscussion() {

    }

    private fun onScreenshotsClicked() {

    }

    private fun onChangeRateStatus(newStatus: RateStatus) {

    }

    private fun onGenreClicked(genre: Genre) {

    }

    private fun onClearHistory() {

    }

    private fun onOpenInBrowser() {

    }

    private fun onWatchOnline() {

    }

    private fun onEditRate() {

    }

    override fun processErrors(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            NetworkException.TAG -> viewState.showNetworkView()
            else -> super.processErrors(throwable)
        }
    }


    private fun processEpisodeErrors(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            NetworkException.TAG -> viewState.showNetworkView()
            ServiceCodeException.TAG -> viewState.setEpisodes(listOf(PlaceholderItem()))
            else -> super.processErrors(throwable)
        }
    }
}