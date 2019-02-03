package com.gnoemes.shikimori.presentation.presenter.manga

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.manga.MangaInteractor
import com.gnoemes.shikimori.domain.ranobe.RanobeInteractor
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.related.RelatedInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.main.BottomScreens
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.search.presentation.SearchNavigationData
import com.gnoemes.shikimori.entity.search.presentation.SearchPayload
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.FranchiseNodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.LinkViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.manga.converter.MangaDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.view.manga.MangaView
import com.gnoemes.shikimori.utils.appendLightLoadingLogic
import com.gnoemes.shikimori.utils.appendLoadingLogic
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

//TODO base details presenter
@InjectViewState
class MangaPresenter @Inject constructor(
        private val mangaInteractor: MangaInteractor,
        private val ranobeInteractor: RanobeInteractor,
        private val ratesInteractor: RatesInteractor,
        private val userInteractor: UserInteractor,
        private val relatedInteractor: RelatedInteractor,
        private val resourceProvider: CommonResourceProvider,
        private val linkConverter: LinkViewModelConverter,
        private val nodeConverter: FranchiseNodeViewModelConverter,
        private val contentConverter: DetailsContentViewModelConverter,
        private val detailsConverter: MangaDetailsViewModelConverter
) : BaseNetworkPresenter<MangaView>() {

    var id: Long = Constants.NO_ID
    var isRanobe: Boolean = false

    private var rateId: Long = Constants.NO_ID
    private var userId: Long = Constants.NO_ID

    private lateinit var currentManga: MangaDetails

    override fun initData() {
        loadUser()
        loadData()
    }

    private fun loadData() =
            loadManga()
                    .doOnSuccess { loadCharacters() }
                    .doOnSuccess { loadSimilar() }
                    .doOnSuccess { loadRelated() }
                    .subscribe({ viewState.setHeadItem(it) }, this::processErrors)
                    .addToDisposables()

    override fun onViewReattached() {
        loadDetails()
                .doOnSuccess { loadOptions() }
                .subscribe({ viewState.setHeadItem(it) }, this::processErrors)
                .addToDisposables()
    }

    private fun loadUser() = userInteractor.getMyUserBrief()
            .doOnSuccess { userId = it.id }
            .subscribe({}, this::processUserErrors)
            .addToDisposables()

    private fun loadDetails(): Single<DetailsHeadItem> =
            (if (isRanobe) ranobeInteractor.getDetails(id)
            else mangaInteractor.getDetails(id))
                    .doOnSuccess { currentManga = it; rateId = it.userRate?.id ?: Constants.NO_ID }
                    .map { detailsConverter.convertHead(it) }

    private fun loadManga(showLoading: Boolean = true): Single<DetailsHeadItem> =
            loadDetails()
                    .flatMap {
                        if (showLoading) Single.just(it).appendLoadingLogic(viewState)
                        else Single.just(it)
                    }
                    .doOnSuccess { loadDescription() }
                    .doOnSuccess { loadOptions() }

    private fun loadCharacters() =
            (if (isRanobe) ranobeInteractor.getRoles(id)
            else mangaInteractor.getRoles(id))
                    .map(contentConverter)
                    .subscribe(
                            { viewState.setContentItem(DetailsContentType.CHARACTERS, it) }, this::processErrors)

    private fun loadSimilar() =
            (if (isRanobe) ranobeInteractor.getSimilar(id)
            else mangaInteractor.getSimilar(id))
                    .map(contentConverter)
                    .subscribe({ viewState.setContentItem(DetailsContentType.SIMILAR, it) }, this::processErrors)

    private fun loadRelated() =
            (if (isRanobe) relatedInteractor.getRanobe(id)
            else relatedInteractor.getManga(id))
                    .map(contentConverter)
                    .subscribe({ viewState.setContentItem(DetailsContentType.RELATED, it) }, this::processErrors)

    private fun loadLinks() =
            (if (isRanobe) ranobeInteractor.getLinks(id)
            else mangaInteractor.getLinks(id))
                    .appendLightLoadingLogic(viewState)
                    .map(linkConverter)
                    .subscribe({
                        if (it.isNotEmpty()) viewState.showLinks(it)
                        else router.showSystemMessage(resourceProvider.emptyMessage)
                    }, this::processErrors)

    private fun loadChronology() =
            (if (isRanobe) ranobeInteractor.getFranchiseNodes(id)
            else mangaInteractor.getFranchiseNodes(id))
                    .appendLightLoadingLogic(viewState)
                    .map(nodeConverter)
                    .subscribe({
                        if (it.isNotEmpty()) viewState.showChronology(it)
                        else router.showSystemMessage(resourceProvider.emptyMessage)
                    }, this::processErrors)

    private fun loadDescription() {
        val descriptionItem = detailsConverter.convertDescriptionItem(currentManga.description)
        viewState.setDescriptionItem(descriptionItem)
    }

    private fun loadOptions() {
        val optionsItem = detailsConverter.convertOptions(currentManga, userId == Constants.NO_ID)
        viewState.setOptionsItem(optionsItem)
    }


    private fun createRate(rate: UserRate?, newStatus: RateStatus? = null) {
        if (userId != Constants.NO_ID) {
            ratesInteractor.createRate(id, type, rate ?: UserRate(status = newStatus), userId)
                    .updateMangaData()
        } else {
            router.showSystemMessage(resourceProvider.needAuth)
        }
    }

    private fun updateRate(rate: UserRate) {
        ratesInteractor.updateRate(rate)
                .updateMangaData()
    }

    private fun changeRateStatus(newStatus: RateStatus) {
        ratesInteractor.changeRateStatus(rateId, newStatus)
                .updateMangaData()
    }

    fun onUpdateOrCreateRate(rate: UserRate) {
        when (rate.id) {
            Constants.NO_ID -> createRate(rate)
            else -> updateRate(rate)
        }
    }

    fun onDeleteRate(id: Long) {
        ratesInteractor.deleteRate(id)
                .updateMangaData()
    }

    fun onAction(action: DetailsAction) {
        when (action) {
            is DetailsAction.Links -> loadLinks()
            is DetailsAction.Chronology -> loadChronology()
            is DetailsAction.WatchOnline -> onWatchOnline()
            is DetailsAction.EditRate -> onEditRate()
            is DetailsAction.OpenInBrowser -> onOpenInBrowser()
            is DetailsAction.ClearHistory -> onClearHistory()
            is DetailsAction.GenreClicked -> onGenreClicked(action.genre)
            is DetailsAction.ChangeRateStatus -> onChangeRateStatus(action.newStatus)
            is DetailsAction.Discussion -> onOpenDiscussion()
        }
    }

    private fun onOpenDiscussion() {
        currentManga.topicId?.let { onTopicClicked(it) } ?:
        //TODO localization
        router.showSystemMessage("Не удалось найти тему")
    }

    private fun onChangeRateStatus(newStatus: RateStatus) {
        when (rateId) {
            Constants.NO_ID -> createRate(null, newStatus)
            else -> changeRateStatus(newStatus)
        }
    }

    private fun onGenreClicked(genre: Genre) {
        router.navigateTo(BottomScreens.SEARCH, SearchNavigationData(SearchPayload(genre), type))
    }

    private fun onClearHistory() {

    }

    private fun onOpenInBrowser() = onOpenWeb(currentManga.url)

    private fun onWatchOnline() {
        //TODO chapters screen
    }

    private fun onEditRate() {
        viewState.showRateDialog(currentManga.userRate)
    }

    private fun processUserErrors(throwable: Throwable) {
        throwable.printStackTrace()
    }

    private val type: Type
        get() = if (isRanobe) Type.RANOBE else Type.MANGA

    private fun Completable.updateMangaData() {
        this.andThen(loadManga(false))
                .doOnSuccess { viewState.setHeadItem(it) }
                .subscribe({ }, this@MangaPresenter::processErrors)
                .addToDisposables()
    }
}