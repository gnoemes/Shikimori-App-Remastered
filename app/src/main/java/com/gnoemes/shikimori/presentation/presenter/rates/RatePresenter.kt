package com.gnoemes.shikimori.presentation.presenter.rates

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.entity.common.presentation.SortItem
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.paginator.PageOffsetPaginator
import com.gnoemes.shikimori.presentation.presenter.common.paginator.ViewController
import com.gnoemes.shikimori.presentation.presenter.common.provider.SortResourceProvider
import com.gnoemes.shikimori.presentation.view.rates.RateView
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class RatePresenter @Inject constructor(
        private val ratesInteractor: RatesInteractor,
        private val sortResourceProvider: SortResourceProvider
) : BaseNetworkPresenter<RateView>(), ViewController<Rate> {

    var userId: Long = Constants.NO_ID
    lateinit var type: Type
    lateinit var rateStatus: RateStatus
    private lateinit var paginator: PageOffsetPaginator<Rate>
    private val isAnime: Boolean
        get() = type == Type.ANIME

    private var isDescendingSort: Boolean = false
    private var items = mutableListOf<Any>()
    private var sort: RateSort = RateSort.Id


    override fun initData() {
        paginator = PageOffsetPaginator({ loadRate(it) }, this)
        paginator.refresh()
    }

    private fun loadRate(page: Int): Single<List<Rate>> =
            if (isAnime) ratesInteractor.getAnimeRates(userId, page, Constants.MAX_LIMIT, rateStatus)
            else ratesInteractor.getMangaRates(userId, page, Constants.MAX_LIMIT, rateStatus)

    override fun showEmptyError(show: Boolean, throwable: Throwable?) {
        if (show) processErrors(throwable!!)
        else viewState.apply {
            hideEmptyView()
            hideNetworkView()
        }
    }

    override fun showEmptyView(show: Boolean) {
        if (show) viewState.showEmptyView()
        else viewState.hideEmptyView()
    }

    override fun showData(show: Boolean, data: List<Rate>) {
        items = data.toMutableList()
        if (show) onSortChanged(sort, isDescendingSort)
        else viewState.hideList()
    }

    override fun showRefreshProgress(show: Boolean) {
        if (show) viewState.showRefresh()
        else viewState.hideRefresh()
    }

    override fun showPageProgress(show: Boolean) {
        viewState.showPageProgress(show)
    }

    override fun showError(throwable: Throwable) {
        processErrors(throwable)
    }

    fun onRefresh() {
        paginator.refresh()
    }

    fun loadNewPage() {
        paginator.loadNewPage()
    }

    fun onAction(it: DetailsAction) {
        when (it) {
            is DetailsAction.ChangeRateStatus -> onChangeRateStatus(it.id, it.newStatus)
            is DetailsAction.EditRate -> onEditRate(it.rate)
        }
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
    }

    private fun onChangeRateStatus(id: Long, newStatus: RateStatus) =
            ratesInteractor.changeRateStatus(id, newStatus)
                    .subscribeAndRefresh()

    fun onDeleteRate(id: Long) =
            ratesInteractor.deleteRate(id)
                    .subscribeAndRefresh()


    fun onUpdateRate(rate: UserRate) =
            ratesInteractor.updateRate(rate)
                    .subscribeAndRefresh()

    private fun Completable.subscribeAndRefresh() {
        this.subscribe(this@RatePresenter::onRefresh, this@RatePresenter::processErrors)
                .addToDisposables()

    }

    fun onSortChanged(sort: RateSort, desc: Boolean) {
        isDescendingSort = desc
        this.sort = sort

        when (sort) {
            is RateSort.Id -> sortAndShow { it.idSort() }
            is RateSort.DateAired -> sortAndShow { it.dateAiredSort() }
            is RateSort.Episodes -> sortAndShow { it.episodesSort() }
            is RateSort.EpisodesWatched -> sortAndShow { it.episodesWatchedSort() }
            is RateSort.Type -> sortAndShow { it.typeSort() }
            is RateSort.Status -> sortAndShow { it.statusSort() }
            is RateSort.Score -> sortAndShow { it.scoreSort() }
            is RateSort.Random -> sortAndShow { it.randomSort() }
        }
    }
//TODO optimization?
    private fun MutableList<Any>.addSortItem(): MutableList<Any> {
        val sorts = if (isAnime) sortResourceProvider.getAnimeRateSorts() else sortResourceProvider.getMangaRateSorts()
        add(0, SortItem(sort, sorts, isDescendingSort))
        return this
    }

    private inline fun sortAndShow(crossinline sortAction: (MutableList<Any>) -> MutableList<Any>) {
        Single.just(items)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map { sortAction.invoke(it) }
                .map { items = it; items }
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

    private fun MutableList<Any>.randomSort(): MutableList<Any> =
            this.filter { it is Rate }
                    .shuffled()
                    .toMutableList()
                    .addSortItem()
}


