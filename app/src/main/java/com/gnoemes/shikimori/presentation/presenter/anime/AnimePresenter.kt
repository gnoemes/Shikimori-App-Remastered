package com.gnoemes.shikimori.presentation.presenter.anime

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.anime.AnimeInteractor
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.related.RelatedInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.*
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.series.presentation.SeriesNavigationData
import com.gnoemes.shikimori.entity.similar.domain.SimilarNavigationData
import com.gnoemes.shikimori.presentation.presenter.anime.converter.AnimeDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.FranchiseNodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.details.BaseDetailsPresenter
import com.gnoemes.shikimori.presentation.view.anime.AnimeView
import com.gnoemes.shikimori.utils.appendLightLoadingLogic
import com.gnoemes.shikimori.utils.appendLoadingLogic
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
open class AnimePresenter @Inject constructor(
        private val animeInteractor: AnimeInteractor,
        private val relatedInteractor: RelatedInteractor,
        private val viewModelConverter: AnimeDetailsViewModelConverter,
        private val settingsSource: SettingsSource,
        ratesInteractor: RatesInteractor,
        userInteractor: UserInteractor,
        resourceProvider: CommonResourceProvider,
        nodeConverter: FranchiseNodeViewModelConverter,
        contentConverter: DetailsContentViewModelConverter
) : BaseDetailsPresenter<AnimeView>(ratesInteractor, userInteractor, resourceProvider, nodeConverter, contentConverter) {

    private lateinit var currentAnime: AnimeDetails

    override val type: Type
        get() = Type.ANIME

    override fun loadContent(showLoading: Boolean): Single<DetailsHeadItem> =
            loadDetails()
                    .flatMap {
                        if (showLoading) Single.just(it).appendLoadingLogic(viewState)
                        else Single.just(it)
                    }
                    .doOnSuccess { loadInfo() }
                    .doOnSuccess { loadActions() }
                    .doOnSuccess { loadVideo() }
                    .doOnSuccess { loadDescription() }
                    .doOnSuccess { loadScreenshots() }

    override fun loadDetails(): Single<DetailsHeadItem> =
            animeInteractor.getDetails(id)
                    .doOnSuccess { currentAnime = it; rateId = it.userRate?.id ?: Constants.NO_ID }
                    .map { viewModelConverter.convertHead(it, userId == Constants.NO_ID) }

    override val characterFactory: (id: Long) -> Single<Roles>
        get() = { animeInteractor.getRoles(it) }

    override val relatedFactory: (id: Long) -> Single<List<Related>>
        get() = { relatedInteractor.getAnime(it) }

    override val linkFactory: (id: Long) -> Single<List<Link>>
        get() = { animeInteractor.getLinks(it) }

    override val chronologyFactory: (id: Long) -> Single<List<FranchiseNode>>
        get() = { animeInteractor.getFranchiseNodes(it) }

    private fun loadInfo() {
        val item = viewModelConverter.convertInfo(currentAnime, creators)
        viewState.setInfoItem(item)
    }

    private fun loadVideo() {
        val items = currentAnime.videos ?: emptyList()
        viewState.setContentItem(DetailsContentType.VIDEO, contentConverter.apply(items))
    }

    private fun loadDescription() {
        val descriptionItem = viewModelConverter.convertDescriptionItem(currentAnime.description)
        viewState.setDescriptionItem(descriptionItem)
    }

    private fun loadActions() {
        val item = viewModelConverter.getActions()
        viewState.setActionItem(item)
    }

    private fun loadScreenshots() =
            animeInteractor.getScreenshots(id)
                    .appendLightLoadingLogic(viewState)
                    .map(contentConverter)
                    .subscribe({viewState.setContentItem(DetailsContentType.SCREENSHOTS, it)}, this::processErrors)
                    .addToDisposables()

    override fun setCreators(creators: List<Pair<Person, List<String>>>) {
        super.setCreators(creators)
        val item = viewModelConverter.convertInfo(currentAnime, creators)
        viewState.setInfoItem(item)
    }

    override fun onOpenDiscussion() {
        currentAnime.topicId?.let { onTopicClicked(it) }
                ?: router.showSystemMessage(resourceProvider.topicNotFound)
        logEvent(AnalyticEvent.ANIME_DETAILS_DISCUSSION)
    }

    override fun onChangeRateStatus(newStatus: RateStatus) {
        super.onChangeRateStatus(newStatus)
        logEvent(AnalyticEvent.RATE_DROP_MENU)
    }

    override fun onStudioClicked(id: Long) {
        super.onStudioClicked(id)
        logEvent(AnalyticEvent.ANIME_DETAILS_STUDIO)
    }

    override fun onGenreClicked(genre: Genre) {
        super.onGenreClicked(genre)
        logEvent(AnalyticEvent.ANIME_DETAILS_GENRE)
    }

    override fun onChronology() {
        super.onChronology()
        logEvent(AnalyticEvent.ANIME_DETAILS_CHRONOLOGY)
    }

    override fun onSimilarClicked() {
        val data = SimilarNavigationData(currentAnime.id, Type.ANIME)
        router.navigateTo(Screens.SIMILAR, data)
    }

    override fun onLinks() {
        super.onLinks()
        logEvent(AnalyticEvent.ANIME_DETAILS_LINKS)
    }

    override fun onOpenInBrowser() = onOpenWeb(currentAnime.url)

    override fun onWatchOnline() {
        val data = SeriesNavigationData(id,
                currentAnime.image,
                currentAnime.nameRu ?: currentAnime.name,
                currentAnime.userRate?.id,
                if (currentAnime.status == Status.RELEASED) currentAnime.episodes else currentAnime.episodesAired,
                null)
        router.navigateTo(Screens.SERIES, data)
        logEvent(AnalyticEvent.NAVIGATION_ANIME_TRANSLATIONS)
    }

    override fun onEditRate() {
        val title = if (settingsSource.isRussianNaming) currentAnime.nameRu
                ?: currentAnime.name else currentAnime.name
        viewState.showRateDialog(title, currentAnime.userRate)
        logEvent(AnalyticEvent.RATE_DIALOG)
    }

    override fun onStatusDialog() {
        val title = if (settingsSource.isRussianNaming) currentAnime.nameRu
                ?: currentAnime.name else currentAnime.name
        viewState.showStatusDialog(id, title, currentAnime.userRate?.status, true)
    }

    override fun onScreenshotsClicked() {

    }

    override fun onClearHistory() {

    }
}