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
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.PlaceholderItem
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.anime.converter.AnimeDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.anime.converter.EpisodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.converter.FranchiseNodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.LinkViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.view.anime.AnimeView
import com.gnoemes.shikimori.utils.appendLightLoadingLogic
import com.gnoemes.shikimori.utils.appendLoadingLogic
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class AnimePresenter @Inject constructor(
        private val animeInteractor: AnimeInteractor,
        private val seriesInteractor: SeriesInteractor,
        private val ratesInteractor: RatesInteractor,
        private val userInteractor: UserInteractor,
        private val relatedInteractor: RelatedInteractor,
        private val resourceProvider: CommonResourceProvider,
        private val viewModelConverter: AnimeDetailsViewModelConverter,
        private val episodeConverter: EpisodeViewModelConverter,
        private val linkConverter: LinkViewModelConverter,
        private val nodeConverter: FranchiseNodeViewModelConverter
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
                    .doOnSuccess { loadCharacters() }
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

    private fun loadAnime(showLoading: Boolean = true) =
            animeInteractor.getDetails(id)
                    .flatMap {
                        if (showLoading) Single.just(it).appendLoadingLogic(viewState)
                        else Single.just(it)
                    }
                    .doOnSuccess { currentAnime = it; rateId = it.userRate?.id ?: Constants.NO_ID }
                    .map(viewModelConverter)

    private fun loadCharacters() =
            animeInteractor.getRoles(id)
                    .map { viewModelConverter.convertCharacters(it) }
                    .subscribe({ viewState.updateCharacters(it) }, this::processErrors)

    private fun loadSimilar() =
            animeInteractor.getSimilar(id)
                    .map { viewModelConverter.convertSimilar(it) }
                    .subscribe({ viewState.updateSimilar(it) }, this::processErrors)

    private fun loadRelated() =
            relatedInteractor.getAnime(id)
                    .map { viewModelConverter.convertRelated(it) }
                    .subscribe({ viewState.updateRelated(it) }, this::processErrors)

    private fun loadLinks() =
            animeInteractor.getLinks(id)
                    .appendLightLoadingLogic(viewState)
                    .map(linkConverter)
                    .subscribe({
                        if (it.isNotEmpty()) viewState.showLinks(it)
                        else router.showSystemMessage(resourceProvider.emptyMessage)
                    }, this::processErrors)

    private fun loadChronology() =
            animeInteractor.getFranchiseNodes(id)
                    .appendLightLoadingLogic(viewState)
                    .map(nodeConverter)
                    .subscribe({
                        if (it.isNotEmpty()) viewState.showChronology(it)
                        else router.showSystemMessage(resourceProvider.emptyMessage)
                    }, this::processErrors)

    private fun createRate(newStatus: RateStatus) {
        if (userId != Constants.NO_ID) {
            ratesInteractor.createRate(id, Type.ANIME, UserRate(status = newStatus), userId)
                    .andThen(loadAnime(false))
                    .subscribe({ viewState.updateHead(it.first()) }, this::processErrors)
                    .addToDisposables()
        }
    }


    private fun updateRate(newStatus: RateStatus) {
        if (userId != Constants.NO_ID) {
            ratesInteractor.changeRateStatus(rateId, newStatus)
                    .andThen(loadAnime(false))
                    .subscribe({ viewState.updateHead(it.first()) }, this::processErrors)
                    .addToDisposables()
        } else {
            //TODO if user not authorized
        }
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
        when (rateId) {
            Constants.NO_ID -> createRate(newStatus)
            else -> updateRate(newStatus)
        }
    }

    private fun onGenreClicked(genre: Genre) {

    }

    private fun onClearHistory() {

    }

    private fun onOpenInBrowser() = onOpenWeb(currentAnime.url)

    private fun onWatchOnline() {

    }

    private fun onEditRate() {
        viewState.showRateDialog(currentAnime.userRate)
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