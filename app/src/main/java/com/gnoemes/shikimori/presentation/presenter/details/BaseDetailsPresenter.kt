package com.gnoemes.shikimori.presentation.presenter.details

import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.*
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.main.BottomScreens
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.search.presentation.SearchNavigationData
import com.gnoemes.shikimori.entity.search.presentation.SearchPayload
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.FranchiseNodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.LinkViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.view.details.BaseDetailsView
import com.gnoemes.shikimori.utils.appendLightLoadingLogic
import io.reactivex.Completable
import io.reactivex.Single

abstract class BaseDetailsPresenter<View : BaseDetailsView>(
        protected val ratesInteractor: RatesInteractor,
        protected val userInteractor: UserInteractor,
        protected val resourceProvider: CommonResourceProvider,
        protected val linkConverter: LinkViewModelConverter,
        protected val nodeConverter: FranchiseNodeViewModelConverter,
        protected val contentConverter: DetailsContentViewModelConverter
) : BaseNetworkPresenter<View>() {

    var id: Long = Constants.NO_ID

    protected var rateId: Long = Constants.NO_ID
    protected var userId: Long = Constants.NO_ID

    override fun initData() {
        loadUser()
        loadData()
    }

    override fun onViewReattached() {
        loadDetails()
                .doOnSuccess { loadOptions() }
                .subscribe({ viewState.setHeadItem(it) }, this::processErrors)
                .addToDisposables()
    }

    abstract fun loadContent(showLoading: Boolean = true): Single<DetailsHeadItem>

    abstract fun loadDetails(): Single<DetailsHeadItem>

    abstract fun loadOptions()

    abstract val type: Type

    abstract val characterFactory: (id: Long) -> Single<List<Character>>

    abstract val similarFactory: (id: Long) -> Single<List<LinkedContent>>

    abstract val relatedFactory: (id: Long) -> Single<List<Related>>

    abstract val linkFactory: (id: Long) -> Single<List<Link>>

    abstract val chronologyFactory: (id: Long) -> Single<List<FranchiseNode>>

    protected open fun loadData() =
            loadContent()
                    .doOnSuccess { loadCharacters() }
                    .doOnSuccess { loadSimilar() }
                    .doOnSuccess { loadRelated() }
                    .subscribe({ viewState.setHeadItem(it) }, this::processErrors)
                    .addToDisposables()

    protected open fun loadCharacters() =
            characterFactory.invoke(id)
                    .map(contentConverter)
                    .subscribe({ viewState.setContentItem(DetailsContentType.CHARACTERS, it) }, this::processErrors)

    protected open fun loadSimilar() =
            similarFactory.invoke(id)
                    .map(contentConverter)
                    .subscribe({ viewState.setContentItem(DetailsContentType.SIMILAR, it) }, this::processErrors)

    protected open fun loadRelated() =
            relatedFactory.invoke(id)
                    .map(contentConverter)
                    .subscribe({ viewState.setContentItem(DetailsContentType.RELATED, it) }, this::processErrors)

    protected open fun loadLinks() =
            linkFactory.invoke(id)
                    .appendLightLoadingLogic(viewState)
                    .map(linkConverter)
                    .subscribe({
                        if (it.isNotEmpty()) viewState.showLinks(it)
                        else router.showSystemMessage(resourceProvider.emptyMessage)
                    }, this::processErrors)

    protected open fun loadChronology() =
            chronologyFactory.invoke(id)
                    .appendLightLoadingLogic(viewState)
                    .map(nodeConverter)
                    .subscribe({
                        if (it.isNotEmpty()) viewState.showChronology(it)
                        else router.showSystemMessage(resourceProvider.emptyMessage)
                    }, this::processErrors)

    protected open fun loadUser() =
            userInteractor.getMyUserId()
                    .doOnSuccess { userId = it }
                    .subscribe({}, this::processUserErrors)
                    .addToDisposables()

    protected open fun createRate(rate: UserRate?, newStatus: RateStatus? = null) {
        if (userId != Constants.NO_ID) {
            ratesInteractor.createRate(id, type, rate ?: UserRate(status = newStatus), userId)
                    .updateContentData()
        } else {
            router.showSystemMessage(resourceProvider.needAuth)
        }
    }

    protected open fun updateRate(rate: UserRate) {
        ratesInteractor.updateRate(rate)
                .updateContentData()
    }

    protected open fun changeRateStatus(newStatus: RateStatus) {
        ratesInteractor.changeRateStatus(rateId, newStatus)
                .updateContentData()
    }

    open fun onUpdateOrCreateRate(rate: UserRate) {
        when (rate.id) {
            Constants.NO_ID -> createRate(rate)
            else -> updateRate(rate)
        }
    }

    open fun onDeleteRate(id: Long) {
        ratesInteractor.deleteRate(id)
                .updateContentData()
    }

    open fun onAction(action: DetailsAction) {
        when (action) {
            is DetailsAction.Links -> onLinks()
            is DetailsAction.Chronology -> onChronology()
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

    protected open fun onChronology() {
        loadChronology()
    }

    protected open fun onLinks() {
        loadLinks()
    }

    protected open fun onStudioClicked(id: Long) {
        router.navigateTo(BottomScreens.SEARCH, SearchNavigationData(SearchPayload(studioId = id), Type.ANIME))
    }

    protected open fun onChangeRateStatus(newStatus: RateStatus) {
        when (rateId) {
            Constants.NO_ID -> createRate(null, newStatus)
            else -> changeRateStatus(newStatus)
        }
    }

    protected open fun onGenreClicked(genre: Genre) {
        router.navigateTo(BottomScreens.SEARCH, SearchNavigationData(SearchPayload(genre), type))
    }

    protected open fun onOpenDiscussion() {
    }

    protected open fun onScreenshotsClicked() {
    }

    protected open fun onClearHistory() {
    }

    protected open fun onOpenInBrowser() {
    }

    protected open fun onWatchOnline() {
    }

    protected open fun onEditRate() {
    }

    protected open fun processUserErrors(throwable: Throwable) {
        throwable.printStackTrace()
    }

    protected open fun Completable.updateContentData() {
        andThen(loadContent(false))
                .doOnSuccess { viewState.setHeadItem(it) }
                .subscribe({ }, this@BaseDetailsPresenter::processErrors)
                .addToDisposables()
    }

}