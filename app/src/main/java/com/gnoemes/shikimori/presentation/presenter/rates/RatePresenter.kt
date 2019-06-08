package com.gnoemes.shikimori.presentation.presenter.rates

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.data.local.preference.RateSortSource
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.rates.RateChangesInteractor
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.ContentException
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.entity.common.presentation.SortAction
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.rates.presentation.RateCategory
import com.gnoemes.shikimori.entity.rates.presentation.RateSortViewModel
import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import com.gnoemes.shikimori.entity.series.presentation.TranslationsNavigationData
import com.gnoemes.shikimori.presentation.presenter.base.BasePaginationPresenter
import com.gnoemes.shikimori.presentation.presenter.common.paginator.ViewController
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.common.provider.SortResourceProvider
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateCountConverter
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateViewModelConverter
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
        private val resourceProvider: CommonResourceProvider,
        private val settingsSource: SettingsSource,
        private val sortSource: RateSortSource,
        private val rateCountConverter: RateCountConverter,
        private val converter: RateViewModelConverter
) : BasePaginationPresenter<Rate, RateView>(), ViewController<Rate> {

    var type: Type = Type.ANIME
    var userId: Long = Constants.NO_ID
    var rateStatus: RateStatus? = null
    var priorityStatus: RateStatus? = null

    private val isAnime: Boolean
        get() = type == Type.ANIME

    private var isDescendingSort: Boolean = false
    private var items = mutableListOf<Any>()
    private var sort: RateSort = RateSort.Id

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
        if (userId != Constants.NO_ID) loadRateCategories()
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
            userInteractor.getMyUserBrief()
                    .doOnSuccess { userId = it.id }
                    .doOnSuccess { loadRateCategories() }
                    .subscribe({ }, this::processUserErrors)

    private fun setRateData(items: List<RateCategory>) {
        viewState.setNavigationItems(items)
        if (items.isNotEmpty()) {
            if (rateStatus == null) onChangeStatus(priorityStatus ?: items.first().status)
            viewState.hideEmptyView()
            viewState.hideNetworkView()
            viewState.showContent(true)
            priorityStatus = null
        } else {
            viewState.showEmptyView()
            viewState.hideNetworkView()
            viewState.showContent(false)
        }
    }

    private fun processUserErrors(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            ContentException.TAG -> {
                viewState.showEmptyView()
                viewState.showContent(false)
            }
        }
    }

    fun onChangeType() {
        rateStatus = null
        this.type = if (type == Type.ANIME) Type.MANGA else Type.ANIME
        viewState.selectType(type)
        destroyPaginator()
        loadRateCategories()
    }

    fun onChangeStatus(rateStatus: RateStatus) {
        this.rateStatus = rateStatus
        viewState.selectRateStatus(rateStatus)
        loadRateCategories()
        loadData()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Rate list logic
    ///////////////////////////////////////////////////////////////////////////

    override fun getPaginatorRequestFactory(): (Int) -> Single<List<Rate>> {
        return if (isAnime) { page: Int -> ratesInteractor.getAnimeRates(userId, page, Constants.MAX_LIMIT, rateStatus!!) }
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

    fun onAction(it: DetailsAction) {
        when (it) {
            is DetailsAction.ChangeRateStatus -> onChangeRateStatus(it.id, it.newStatus)
            is DetailsAction.EditRate -> onEditRate(it.rate)
            is DetailsAction.WatchOnline -> onWatchOnline(it.id!!)
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

    private fun onEditRate(rate: Rate?) {
        val userRate = UserRate(
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

        viewState.showRateDialog(userRate)
        logEvent(AnalyticEvent.RATE_DIALOG)
    }

    //TODO add manga
    private fun onWatchOnline(rateId: Long) {
        val rateItem = items.find { it is Rate && it.id == rateId } as? Rate
        rateItem?.let { rate ->
            seriesInteractor.getFirstNotWatchedEpisodeIndex(rate.anime?.id!!)
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
        val isAuto = settings.lastAuthor != null && settings.lastType != null
        val name = if (settingsSource.isRussianNaming) rate.anime?.nameRu
                ?: rate.anime?.name!! else rate.anime?.name!!
        val navigationData = TranslationsNavigationData(settings.animeId, rate.anime?.image!!, name, progress.toLong(), progress, rate.id, false, isAuto)
        router.navigateTo(Screens.TRANSLATIONS, navigationData)
    }

    private fun onChangeRateStatus(id: Long, newStatus: RateStatus) {
        ratesInteractor.changeRateStatus(id, newStatus)
                .subscribeAndRefresh(id)
        logEvent(AnalyticEvent.RATE_DROP_MENU)
    }

    fun onDeleteRate(id: Long) =
            ratesInteractor.deleteRate(id)
                    .subscribeAndRefresh(id)

    fun onUpdateRate(rate: UserRate) =
            ratesInteractor.updateRate(rate)
                    .subscribeAndRefresh(rate.id!!)

    private fun Completable.subscribeAndRefresh(id: Long) {
        this.andThen(changesInteractor.sendRateChanges(id))
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
            is RateSort.EpisodesWatched -> sortAndShow { it.episodesWatchedSort() }
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

    private inline fun sortAndShow(crossinline sortAction: (MutableList<Any>) -> MutableList<Any>) {
        Single.just(items)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map { sortAction.invoke(it) }
                .map { items = it; items }
                .map(converter)
                .subscribe({ viewState.showData(it) }, this::processErrors)
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
}



