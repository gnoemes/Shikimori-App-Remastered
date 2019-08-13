package com.gnoemes.shikimori.presentation.presenter.details

import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.*
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.common.presentation.PlaceholderItem
import com.gnoemes.shikimori.entity.main.BottomScreens
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.search.presentation.SearchNavigationData
import com.gnoemes.shikimori.entity.search.presentation.SearchPayload
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.FranchiseNodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.LinkViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.view.details.BaseDetailsView
import com.gnoemes.shikimori.utils.appendLightLoadingLogic
import com.gnoemes.shikimori.utils.clearAndAddAll
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

    protected val characters = mutableListOf<Character>()
    protected val creators = mutableListOf<Pair<Person, List<String>>>()

    override fun initData() {
        loadUser()
        loadData()
    }

    override fun onViewReattached() {
        loadDetails()
                .subscribe({ viewState.setHeadItem(it) }, this::processErrors)
                .addToDisposables()
    }

    abstract fun loadContent(showLoading: Boolean = true): Single<DetailsHeadItem>

    abstract fun loadDetails(): Single<DetailsHeadItem>

    abstract val type: Type

    abstract val characterFactory: (id: Long) -> Single<Roles>

    abstract val relatedFactory: (id: Long) -> Single<List<Related>>

    abstract val linkFactory: (id: Long) -> Single<List<Link>>

    abstract val chronologyFactory: (id: Long) -> Single<List<FranchiseNode>>

    protected open fun loadData() =
            loadContent()
                    .doOnSuccess { loadCharacters() }
                    .doOnSuccess { loadRelated() }
                    .subscribe({ viewState.setHeadItem(it) }, this::processErrors)
                    .addToDisposables()

    protected open fun loadCharacters() =
            characterFactory.invoke(id)
                    .doOnSuccess { setCreators(it.persons) }
                    .map { it.characters }
                    .doOnSuccess { characters.clearAndAddAll(it) }
                    .map(contentConverter)
                    .subscribe({ viewState.setContentItem(DetailsContentType.CHARACTERS, it) }, this::processErrors)
                    .addToDisposables()

    protected open fun loadRelated() =
            relatedFactory.invoke(id)
                    .map(contentConverter)
                    .subscribe({ viewState.setContentItem(DetailsContentType.RELATED, it) }, this::processErrors)
                    .addToDisposables()

    protected open fun loadLinks() =
            linkFactory.invoke(id)
                    .appendLightLoadingLogic(viewState)
                    .map(linkConverter)
                    .subscribe({
                        if (it.isNotEmpty()) viewState.showLinks(it)
                        else router.showSystemMessage(resourceProvider.emptyMessage)
                    }, this::processErrors)
                    .addToDisposables()

    protected open fun loadChronology() =
            chronologyFactory.invoke(id)
                    .appendLightLoadingLogic(viewState)
                    .map(nodeConverter)
                    .subscribe({
                        if (it.isNotEmpty()) viewState.showChronology(it)
                        else router.showSystemMessage(resourceProvider.emptyMessage)
                    }, this::processErrors)
                    .addToDisposables()

    protected open fun loadUser() =
            userInteractor.getMyUserId()
                    .doOnSuccess { userId = it }
                    .subscribe({}, this::processUserErrors)
                    .addToDisposables()

    protected open fun setCreators(creators: List<Pair<Person, List<String>>>) {
        this.creators.clearAndAddAll(creators)
    }

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
            is DetailsAction.RateStatusDialog -> onStatusDialog()
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

    open fun onChangeRateStatus(newStatus: RateStatus) {
        when (rateId) {
            Constants.NO_ID -> createRate(null, newStatus)
            else -> changeRateStatus(newStatus)
        }
    }

    open fun onCharacterSearch(newText: String?) {
        if (newText.isNullOrBlank()) viewState.setContentItem(DetailsContentType.CHARACTERS, contentConverter.apply(characters))
        else {
            val searchItems : MutableList<Any> = characters.filter { it.name.contains(newText, true) || it.nameRu?.contains(newText, true) ?: false }.toMutableList()
            if (searchItems.isEmpty()) searchItems.add(PlaceholderItem())
            viewState.setContentItem(DetailsContentType.CHARACTERS, contentConverter.apply(searchItems))
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

    protected open fun onStatusDialog() {
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