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
import com.gnoemes.shikimori.entity.chronology.ChronologyNavigationData
import com.gnoemes.shikimori.entity.common.domain.*
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.series.presentation.SeriesNavigationData
import com.gnoemes.shikimori.entity.user.domain.Statistic
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem
import com.gnoemes.shikimori.presentation.presenter.anime.converter.AnimeDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.details.BaseDetailsPresenter
import com.gnoemes.shikimori.presentation.view.anime.AnimeView
import com.gnoemes.shikimori.utils.appendLightLoadingLogic
import com.gnoemes.shikimori.utils.appendLoadingLogic
import com.gnoemes.shikimori.utils.applySingleSchedulers
import io.reactivex.Single
import io.reactivex.functions.BiFunction
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
        contentConverter: DetailsContentViewModelConverter
) : BaseDetailsPresenter<AnimeView>(ratesInteractor, userInteractor, resourceProvider, contentConverter) {

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
        val item = viewModelConverter.getActions(currentAnime.status)
        viewState.setActionItem(item)
    }

    private fun loadScreenshots() =
            animeInteractor.getScreenshots(id)
                    .appendLightLoadingLogic(viewState)
                    .map(contentConverter)
                    .subscribe({ viewState.setContentItem(DetailsContentType.SCREENSHOTS, it) }, this::processErrors)
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
        val data = ChronologyNavigationData(id, type, currentAnime.franchise)
        router.navigateTo(Screens.CHRONOLOGY, data)
        logEvent(AnalyticEvent.ANIME_DETAILS_CHRONOLOGY)
    }

    override fun onSimilarClicked() {
        val data = CommonNavigationData(currentAnime.id, Type.ANIME)
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
        viewState.showRateDialog(title, currentAnime.userRate)
        logEvent(AnalyticEvent.RATE_DIALOG)
    }

    override fun onStatusDialog() {
        viewState.showStatusDialog(id, title, currentAnime.userRate?.status, true)
    }

    override fun onScreenshotsClicked() {

    }

    override fun onClearHistory() {

    }

    override fun onStatisticClicked() {
        Single.zip(Single.just(currentAnime.rateScoresStats), Single.just(currentAnime.rateStatusesStats), BiFunction<List<Statistic>, List<Statistic>, Pair<List<UserStatisticItem>, List<UserStatisticItem>>> { t1, t2 ->
            val scores = viewModelConverter.convertScores(t1)
            val statuses = viewModelConverter.convertStatuses(t2)
            Pair(scores, statuses)
        })
                .applySingleSchedulers()
                .subscribe({ viewState.showStatistic(title, it.first, it.second) }, this::processErrors)
                .addToDisposables()
    }

    private val title: String
        get() = if (settingsSource.isRussianNaming) currentAnime.nameRu
                ?: currentAnime.name else currentAnime.name
}