package com.gnoemes.shikimori.presentation.presenter.rates

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.data.local.preference.RateSortSource
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.app.CancelableTaskInteractor
import com.gnoemes.shikimori.domain.rates.PinnedRateInteractor
import com.gnoemes.shikimori.domain.rates.RateChangesInteractor
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.Task
import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.ContentException
import com.gnoemes.shikimori.entity.auth.AuthType
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.entity.common.presentation.SortAction
import com.gnoemes.shikimori.entity.main.BottomScreens
import com.gnoemes.shikimori.entity.rates.domain.*
import com.gnoemes.shikimori.entity.rates.presentation.RateCategory
import com.gnoemes.shikimori.entity.rates.presentation.RateSortViewModel
import com.gnoemes.shikimori.entity.rates.presentation.RateViewModel
import com.gnoemes.shikimori.entity.search.presentation.SearchNavigationData
import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import com.gnoemes.shikimori.entity.series.presentation.SeriesNavigationData
import com.gnoemes.shikimori.presentation.presenter.base.BasePaginationPresenter
import com.gnoemes.shikimori.presentation.presenter.common.paginator.ViewController
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.common.provider.SortResourceProvider
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateCountConverter
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.rates.provider.RateResourceProvider
import com.gnoemes.shikimori.presentation.view.rates.RateView
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class RatePresenter @Inject constructor(
        private val userInteractor: UserInteractor,
        private val ratesInteractor: RatesInteractor,
        private val seriesInteractor: SeriesInteractor,
        private val sortResourceProvider: SortResourceProvider,
        private val changesInteractor: RateChangesInteractor,
        private val taskInteractor: CancelableTaskInteractor,
        private val pinInteractor: PinnedRateInteractor,
        private val resourceProvider: CommonResourceProvider,
        private val settingsSource: SettingsSource,
        private val sortSource: RateSortSource,
        private val rateCountConverter: RateCountConverter,
        private val rateResourceProvider: RateResourceProvider,
        private val converter: RateViewModelConverter
) : BasePaginationPresenter<Rate, RateView>(), ViewController<Rate> {

    var type: Type = Type.ANIME
    var userId: Long = Constants.NO_ID
    var rateStatus: RateStatus? = null
    var priorityStatus: RateStatus? = null

    private val isAnime: Boolean
        get() = type == Type.ANIME

    private val isUserExist: Boolean
        get() = userId != Constants.NO_ID


    private var isDescendingSort: Boolean = false
    private var items = mutableListOf<Any>()
    private var sort: RateSort = RateSort.Id
    private var query: String? = null
    private var pinnedRates: Int = 0

    private var localRemovedItems = mutableListOf<Long>()

    override fun onViewReattached() {
        loadUserOrCategories()
        if (rateStatus != null) onRefresh()
    }

    override fun initData() {
        viewState.setNavigationItems(emptyList())

        sort = sortSource.getSort(type)
        isDescendingSort = sortSource.getOrder(type)

        viewState.selectType(type)
        loadUserOrCategories()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Rate categories logic
    ///////////////////////////////////////////////////////////////////////////

    private fun loadUserOrCategories() {
        if (isUserExist) loadRateCategories()
        else loadMyUser()
    }

    private fun loadRateCategories() =
            userInteractor.getDetails(userId)
                    .map {
                        if (isAnime) rateCountConverter.countAnimeRates(it)
                        else rateCountConverter.countMangaRates(it)
                    }
                    .subscribe(this::setRateData, this::processErrors)
                    .addToDisposables()


    private fun loadMyUser() =
            userInteractor.getMyUserId()
                    .doOnSuccess { userId = it }
                    .doOnSuccess { loadRateCategories() }
                    .subscribe({ }, this::processUserErrors)

    private fun setRateData(items: List<RateCategory>) {
        viewState.setNavigationItems(items)
        viewState.showNeedAuthView(false)
        if (items.isNotEmpty()) {
            if (rateStatus == null) onChangeStatus(priorityStatus ?: items.first().status)
            viewState.showEmptyRatesView(false)
            viewState.hideNetworkView()
            viewState.showContent(true)
            priorityStatus = null
        } else {
            viewState.showEmptyRatesView(true, isAnime)
            viewState.hideNetworkView()
            viewState.showContent(false)
        }
    }

    private fun processUserErrors(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            ContentException.TAG -> {
                viewState.showNeedAuthView(true)
                viewState.showContent(false)
            }
        }
    }

    fun onChangeType() {
        if (isUserExist) {
            destroyPaginator()
            rateStatus = null
            this.type = if (type == Type.ANIME) Type.MANGA else Type.ANIME
            viewState.selectType(type)
            localRemovedItems.clear()
            loadRateCategories()
        }
    }

    fun onChangeStatus(rateStatus: RateStatus) {
        if (isUserExist) {
            this.rateStatus = rateStatus
            viewState.scrollToTop()
            viewState.selectRateStatus(rateStatus)
            localRemovedItems.clear()
            loadData()
        }
    }

    fun onEmptyRateClicked(anime: Boolean) {
        router.navigateTo(BottomScreens.SEARCH, SearchNavigationData(null, if (anime) Type.ANIME else Type.MANGA))
    }

    fun onTaskCanceled(taskId: Int, rateId: Long) {
        localRemovedItems.remove(rateId)

        taskInteractor.cancelTask(taskId)
                .subscribe(this::onRefresh, this::processErrors)
                .addToDisposables()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Rate list logic
    ///////////////////////////////////////////////////////////////////////////

    override fun getPaginatorRequestFactory(): (Int) -> Single<List<Rate>> {
        return if (rateStatus == null) { page: Int -> Single.error(IllegalStateException()) }
        else if (isAnime) { page: Int -> ratesInteractor.getAnimeRates(userId, page, Constants.MAX_LIMIT, rateStatus!!) }
        else { page: Int -> ratesInteractor.getMangaRates(userId, page, Constants.MAX_LIMIT, rateStatus!!) }
    }

    override fun showEmptyError(show: Boolean, throwable: Throwable?) {
        if (show) processErrors(throwable!!)
        else viewState.apply {
            hideEmptyView()
            hideNetworkView()
        }
    }

    override fun showData(show: Boolean, data: List<Rate>) {
        items = data.toMutableList()
        if (show) onSortChanged(sort, isDescendingSort)
        else viewState.showContent(show)
    }

    override fun showEmptyView(show: Boolean) {
        if (show) {
            rateStatus = null
            loadUserOrCategories()
        }
    }

    private fun localRefresh() {
        onSortChanged(sort)
    }

    private fun removeFromListAndRefresh(item: Rate) {
        localRemovedItems.add(item.id)
        items.remove(item)
        localRefresh()
    }

    fun onQueryChanged(newText: String?) {
        query = newText
        localRefresh()
    }

    fun onAction(it: DetailsAction) {
        when (it) {
            is DetailsAction.ChangeRateStatus -> onChangeRateStatus(it.id, it.newStatus)
            is DetailsAction.WatchOnline -> onWatchOnline(it.id!!)
        }
    }

    fun onListAction(it: RateListAction) {
        when (it) {
            is RateListAction.Pin -> onPinRate(it.rate)
            is RateListAction.ChangeOrder -> onPinOrderChanged(it.rate, it.newOrder)
            is RateListAction.SwipeAction -> onRateSwipeAction(it.rate, it.action)
            is RateListAction.EditRate -> onEditRate(it.rate)
        }
    }

    private fun onRateSwipeAction(rate: RateViewModel, action: RateSwipeAction) {
        val rateItem = items.filter { it is Rate }.find { (it as Rate).id == rate.id } as? Rate
        if (rateItem != null) {
            when (action) {
                RateSwipeAction.INCREMENT -> onIncrementRate(rateItem)
                RateSwipeAction.ON_HOLD -> onChangeRateStatus(rateItem.id, RateStatus.ON_HOLD)
                RateSwipeAction.DROP -> onChangeRateStatus(rateItem.id, RateStatus.DROPPED)
                RateSwipeAction.CHANGE -> onShowStatusDialog(rate)
                else -> Unit
            }
        }
    }

    private fun onShowStatusDialog(rate: RateViewModel) {
        viewState.showStatusDialog(rate.id, rate.name, rate.rawRate.status, rate.type == Type.ANIME)
        localRefresh()
    }

    private fun onIncrementRate(rate: Rate) {
        ratesInteractor.increment(rate.id)
                .subscribe({ onRefresh() }, this::processErrors)
                .addToDisposables()
    }

    private fun onPinOrderChanged(rate: RateViewModel, newOrder: Int) {
        pinInteractor.addPinnedRate(PinnedRate(rate.contentId, rate.type, rateStatus!!, newOrder))
                .subscribe({}, this::processErrors)
                .addToDisposables()
    }

    private fun onPinRate(rate: RateViewModel) {
        if (rate.isPinned) pinInteractor.removePinnedRate(rate.contentId)
                .subscribe(this::localRefresh, this::processErrors)
                .addToDisposables()
        else {
            if (pinnedRates >= Constants.MAX_PINNED_RATES) viewState.showPinLimitMessage()
            else pinInteractor.addPinnedRate(PinnedRate(rate.contentId, type, rateStatus!!, if (pinnedRates == 0) 0 else pinnedRates - 1))
                    .subscribe(this::localRefresh, this::processErrors)
                    .addToDisposables()
        }
    }

    fun onSortAction(it: SortAction) {
        when (it) {
            is SortAction.ChangeOrder -> onSortChanged(sort, it.isDescending)
            is SortAction.ChangeSort -> onShowSortDialog(it.sorts)
        }
    }

    private fun onShowSortDialog(sorts: List<Triple<RateSort, String, Boolean>>) {
        viewState.showSortDialog(sorts)
    }

    fun onOpenRandom() {
        val item = items.shuffled().firstOrNull { it is Rate } as? Rate
        if (item != null) {
            if (item.anime != null) onAnimeClicked(item.anime.id)
            else if (item.manga != null) onMangaClicked(item.manga.id)
        } else router.showSystemMessage(resourceProvider.emptyMessage)
    }

    private fun onEditRate(rate: RateViewModel) {
        val userRate = getUserRate(rate.rawRate)
        viewState.showRateDialog(rate.name, userRate)
        logEvent(AnalyticEvent.RATE_DIALOG)
    }

    private fun getUserRate(rate: Rate?) = UserRate(
            id = rate?.id,
            targetId = rate?.anime?.id ?: rate?.manga?.id,
            targetType = type,
            score = rate?.score?.toDouble(),
            status = rateStatus,
            userId = userId,
            episodes = rate?.episodes,
            chapters = rate?.chapters,
            text = rate?.text
    )

    //TODO add manga
    private fun onWatchOnline(rateId: Long) {
        val rateItem = items.find { it is Rate && it.id == rateId } as? Rate
        rateItem?.let { rate ->
            seriesInteractor.getFirstNotWatchedEpisodeIndex(rate.anime?.id!!)
                    .flatMap { episodeIndex ->
                        if (episodeIndex <= rate.episodes!!) seriesInteractor.getWatchedEpisodesCount(rate.anime.id)
                                .map { watchedCount ->
                                    if (watchedCount < rate.episodes) {
                                        if (rate.anime.episodesAired != 0 && (rate.episodes + 1 > rate.anime.episodesAired && rate.episodes + 1 > rate.anime.episodes)) rate.anime.episodes
                                        else rate.episodes + 1
                                    } else episodeIndex
                                }
                        else if (episodeIndex > rate.anime.episodes && rate.anime.episodes != 0) Single.just(rate.anime.episodes)
                        else Single.just(episodeIndex)
                    }
                    .subscribe({ checkRateWatchProgress(true, rate, it) }, this::processErrors)
                    .addToDisposables()
        }
    }

    //TODO add manga
    private fun checkRateWatchProgress(anime: Boolean, rate: Rate, progress: Int) =
            seriesInteractor.getTranslationSettings(rate.anime?.id!!)
                    .subscribe({ watchOnlineOrOpenList(rate, it, progress) }, this::processErrors)
                    .addToDisposables()

    //TODO manga
    private fun watchOnlineOrOpenList(rate: Rate, settings: TranslationSetting, progress: Int) {
        val name =
                if (settingsSource.isRussianNaming) rate.anime?.nameRu ?: rate.anime?.name!!
                else rate.anime?.name!!
        val episodesAired = if (rate.anime?.status == Status.RELEASED) rate.anime.episodes else rate.anime?.episodesAired
        val navigationData = SeriesNavigationData(settings.animeId, rate.anime?.image!!, name, rate.id, episodesAired!!, progress)
        router.navigateTo(Screens.SERIES, navigationData)
        analyticInteractor.logEvent(AnalyticEvent.NAVIGATION_ANIME_TRANSLATIONS)
    }

    fun onChangeRateStatus(id: Long, newStatus: RateStatus) {
        val item = items.find { (it as? Rate)?.id == id } as? Rate
        if (item != null) {
            if (item.status == newStatus) {
                localRefresh()
                return
            }

            val task = Task {
                ratesInteractor.changeRateStatus(id, newStatus)
                        .subscribeAndRefresh(id)
                logEvent(AnalyticEvent.RATE_DROP_MENU)
            }
            taskInteractor.newTask(task)
                    .doOnNext { viewState.showRateMessage(it, rateResourceProvider.getChangeRateStatusMessage(item.type, newStatus), item.id) }
                    .subscribe({ removeFromListAndRefresh(item) }, this::processErrors)
                    .addToDisposables()
        }
    }

    fun onDeleteRate(id: Long) {
        val item = items.find { (it as? Rate)?.id == id } as? Rate
        if (item != null) {
            val task = Task {
                ratesInteractor.deleteRate(id)
                        .subscribeAndRefresh(id)
            }
            taskInteractor.newTask(task)
                    .doOnNext { viewState.showRateMessage(it, rateResourceProvider.getDeleteRateMessage(item.type), item.id) }
                    .subscribe({ removeFromListAndRefresh(item) }, this::processErrors)
                    .addToDisposables()
        }
    }

    fun onUpdateRate(rate: UserRate) =
            ratesInteractor.updateRate(rate)
                    .subscribeAndRefresh(rate.id!!)

    private fun Completable.subscribeAndRefresh(id: Long) {
        this.andThen(changesInteractor.sendRateChanges(id))
                .doOnComplete { loadUserOrCategories() }
                .subscribe(this@RatePresenter::onRefresh, this@RatePresenter::processErrors)
                .addToDisposables()
    }

    fun onSortChanged(sort: RateSort) {
        onSortChanged(sort, isDescendingSort)
    }

    private fun onSortChanged(sort: RateSort, desc: Boolean) {
        isDescendingSort = desc
        this.sort = sort
        sortSource.saveSort(type, sort)
        sortSource.saveOrder(type, desc)

        when (sort) {
            is RateSort.Id -> sortAndShow { it.idSort() }
            is RateSort.DateAired -> sortAndShow { it.dateAiredSort() }
            is RateSort.Episodes -> sortAndShow { it.episodesSort() }
            is RateSort.Progress -> sortAndShow { it.episodesWatchedSort() }
            is RateSort.Type -> sortAndShow { it.typeSort() }
            is RateSort.Status -> sortAndShow { it.statusSort() }
            is RateSort.Score -> sortAndShow { it.scoreSort() }
            is RateSort.Name -> sortAndShow { it.nameSort() }
        }
    }

    //TODO optimization?
    private fun MutableList<Any>.addSortItem(): MutableList<Any> {
        val sorts = if (isAnime) sortResourceProvider.getAnimeRateSorts() else sortResourceProvider.getMangaRateSorts()
        add(0, RateSortViewModel(rateStatus!!, sort, sorts, isDescendingSort, isAnime))
        return this
    }

    private fun searchItems(it: MutableList<Any>): MutableList<Any> {
        val sortItem = it.first()
        return mutableListOf(sortItem)
                .union(
                        it.filter { it is Rate }
                                .map { it as Rate }
                                .filter {
                                    if (it.type == Type.ANIME) it.anime?.name?.contains(query!!, ignoreCase = true) ?: false || it.anime?.nameRu?.contains(query!!, ignoreCase = true) ?: false
                                    else it.manga?.name?.contains(query!!, ignoreCase = true) ?: false || it.manga?.nameRu?.contains(query!!, ignoreCase = true) ?: false
                                }
                )
                .toMutableList()
    }

    private fun setData(it: List<Any>) {
        if (query.isNullOrBlank()) pinnedRates = it.filter { it is RateViewModel }.count { (it as RateViewModel).isPinned }
        if (!query.isNullOrBlank() && (it.size == 1 && it.first() is RateSortViewModel)) viewState.showEmptySearchView(it)
        else viewState.showData(it)

    }

    private inline fun sortAndShow(crossinline sortAction: (MutableList<Any>) -> MutableList<Any>) {
        Single.just(items)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map { sortAction.invoke(it) }
                .map { items = it; items }
                .map { if (!query.isNullOrBlank()) searchItems(it) else it }
                .flatMap { rates ->
                    pinInteractor.getPinnedRates(type, rateStatus ?: RateStatus.WATCHING)
                            .map { converter.apply(rates, it) }
                }
                .map { list ->
                    list.filter {
                        if (it is RateViewModel) !localRemovedItems.contains(it.id)
                        else false
                    }
                }
                .subscribe({ setData(it) }, this::processErrors)
                .addToDisposables()
    }

    private inline fun <R : Comparable<R>> MutableList<Any>.rateSort(desc: Boolean = isDescendingSort, crossinline selector: (Rate) -> R?): MutableList<Any> =
            if (desc) this.asSequence()
                    .filter { it is Rate }
                    .map { it as Rate }
                    .sortedByDescending {
                        selector.invoke(it)
                    }.toMutableList()
            else this.asSequence()
                    .filter { it is Rate }
                    .map { it as Rate }
                    .sortedBy {
                        selector.invoke(it)
                    }.toMutableList()

    private inline fun <R : Comparable<R>> MutableList<Any>.sortRateBySelectorAndAddItem(crossinline selector: (Rate) -> R?): MutableList<Any> =
            this.rateSort { selector.invoke(it) }.addSortItem()

    private fun MutableList<Any>.idSort(): MutableList<Any> =
            sortRateBySelectorAndAddItem {
                if (it.type == Type.ANIME) it.anime?.id
                else it.manga?.id
            }

    private fun MutableList<Any>.dateAiredSort(): MutableList<Any> =
            this.sortRateBySelectorAndAddItem {
                if (it.type == Type.ANIME) it.anime?.dateAired
                else it.manga?.dateAired
            }

    private fun MutableList<Any>.episodesSort(): MutableList<Any> =
            this.sortRateBySelectorAndAddItem {
                if (it.type == Type.ANIME) it.anime?.episodes
                else it.manga?.chapters
            }

    private fun MutableList<Any>.episodesWatchedSort(): MutableList<Any> =
            this.sortRateBySelectorAndAddItem {
                if (it.type == Type.ANIME) it.episodes
                else it.chapters
            }

    private fun MutableList<Any>.scoreSort(): MutableList<Any> =
            this.sortRateBySelectorAndAddItem { it.score }

    private fun MutableList<Any>.typeSort(): MutableList<Any> =
            this.sortRateBySelectorAndAddItem {
                if (it.type == Type.ANIME) it.anime?.type?.ordinal!!
                else it.manga?.type?.ordinal!!
            }

    private fun MutableList<Any>.statusSort(): MutableList<Any> =
            this.sortRateBySelectorAndAddItem {
                if (it.type == Type.ANIME) it.anime?.status?.ordinal
                else it.manga?.status?.ordinal!!
            }

    private fun MutableList<Any>.nameSort(): MutableList<Any> =
            this.sortRateBySelectorAndAddItem {
                if (it.type == Type.ANIME) if (isRussianNaming) it.anime?.nameRu else it.anime?.name
                else if (isRussianNaming) it.manga?.nameRu else it.manga?.name
            }

    private val isRussianNaming: Boolean
        get() = settingsSource.isRussianNaming

    fun onSignIn() = openAuth(AuthType.SIGN_IN)
    fun onSignUp() = openAuth(AuthType.SIGN_UP)

    private fun openAuth(type: AuthType) {
        router.navigateTo(Screens.AUTHORIZATION, type)
        logEvent(AnalyticEvent.NAVIGATION_AUTHORIZATION)
    }
}



