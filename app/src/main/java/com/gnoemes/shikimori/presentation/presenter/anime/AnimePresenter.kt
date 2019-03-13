package com.gnoemes.shikimori.presentation.presenter.anime

import com.arellomobile.mvp.InjectViewState
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
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.series.presentation.EpisodesNavigationData
import com.gnoemes.shikimori.presentation.presenter.anime.converter.AnimeDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.FranchiseNodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.LinkViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.details.BaseDetailsPresenter
import com.gnoemes.shikimori.presentation.view.anime.AnimeView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class AnimePresenter @Inject constructor(
        private val animeInteractor: AnimeInteractor,
        private val relatedInteractor: RelatedInteractor,
        private val viewModelConverter: AnimeDetailsViewModelConverter,
        ratesInteractor: RatesInteractor,
        userInteractor: UserInteractor,
        resourceProvider: CommonResourceProvider,
        linkConverter: LinkViewModelConverter,
        nodeConverter: FranchiseNodeViewModelConverter,
        contentConverter: DetailsContentViewModelConverter
) : BaseDetailsPresenter<AnimeView>(ratesInteractor, userInteractor, resourceProvider, linkConverter, nodeConverter, contentConverter) {

    private lateinit var currentAnime: AnimeDetails

    override val type: Type
        get() = Type.ANIME

    override fun loadContent(showLoading: Boolean): Single<DetailsHeadItem> =
            loadDetails()
                    .flatMap {
                        if (showLoading) Single.just(it).appendLoadingLogic(viewState)
                        else Single.just(it)
                    }
                    .doOnSuccess { loadVideo() }
                    .doOnSuccess { loadDescription() }
                    .doOnSuccess { loadOptions() }

    override fun loadDetails(): Single<DetailsHeadItem> =
            animeInteractor.getDetails(id)
                    .doOnSuccess { currentAnime = it; rateId = it.userRate?.id ?: Constants.NO_ID }
                    .map { viewModelConverter.convertHead(it) }

    override val characterFactory: (id: Long) -> Single<List<Character>>
        get() = { animeInteractor.getRoles(it) }

    override val similarFactory: (id: Long) -> Single<List<LinkedContent>>
        get() = { animeInteractor.getSimilar(it).map { it.map { it as LinkedContent } } }

    override val relatedFactory: (id: Long) -> Single<List<Related>>
        get() = { relatedInteractor.getAnime(it) }

    override val linkFactory: (id: Long) -> Single<List<Link>>
        get() = { animeInteractor.getLinks(it) }

    override val chronologyFactory: (id: Long) -> Single<List<FranchiseNode>>
        get() = { animeInteractor.getFranchiseNodes(it) }


    private fun loadVideo() {
        val items = currentAnime.videos ?: emptyList()
        viewState.setContentItem(DetailsContentType.VIDEO, contentConverter.apply(items))
    }

    private fun loadDescription() {
        val descriptionItem = viewModelConverter.convertDescriptionItem(currentAnime.description)
        viewState.setDescriptionItem(descriptionItem)
    }

    override fun loadOptions() {
        val optionsItem = viewModelConverter.convertOptions(currentAnime, userId == Constants.NO_ID)
        viewState.setOptionsItem(optionsItem)
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

    override fun onLinks() {
        super.onLinks()
        logEvent(AnalyticEvent.ANIME_DETAILS_LINKS)
    }

    override fun onOpenInBrowser() = onOpenWeb(currentAnime.url)

    override fun onWatchOnline() {
        val data = EpisodesNavigationData(id, currentAnime.image, currentAnime.nameRu
                ?: currentAnime.name, currentAnime.userRate?.id)
        router.navigateTo(Screens.EPISODES, data)
        logEvent(AnalyticEvent.NAVIGATION_ANIME_EPISODES)
    }

    override fun onEditRate() {
        viewState.showRateDialog(currentAnime.userRate)
        logEvent(AnalyticEvent.RATE_DIALOG)
    }

    override fun onScreenshotsClicked() {

    }

    override fun onClearHistory() {

    }
}