package com.gnoemes.shikimori.presentation.presenter.search

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.search.SearchInteractor
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.paginator.PageOffsetPaginator
import com.gnoemes.shikimori.presentation.presenter.common.paginator.ViewController
import com.gnoemes.shikimori.presentation.presenter.search.converter.SearchViewModelConverter
import com.gnoemes.shikimori.presentation.view.search.SearchView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import io.reactivex.Single
import javax.inject.Inject

//TODO studios
@InjectViewState
class SearchPresenter @Inject constructor(
        private val interactor: SearchInteractor,
        private val converter: SearchViewModelConverter
) : BaseNetworkPresenter<SearchView>() {

    private var paginator: PageOffsetPaginator<SearchItem>? = null

    var genre: Genre? = null
    var type: Type = Type.ANIME
    var filters = HashMap<String, MutableList<FilterItem>>()

    private val supportsPagination = listOf(Type.ANIME, Type.MANGA, Type.RANOBE)

    override fun initData() {
        loadData()
    }

    private fun loadData() {
        if (isPagination()) loadWithPaginator()
        else loadSimple()
    }

    private fun loadSimple() {
        if (filters.isEmpty()) {
            viewState.apply { hideData(); setSimpleEmptyText(); showEmptyView() }
        } else {
            getSimpleRequestFactory()
                    .appendLoadingLogic(viewState)
                    .subscribe({ viewController.showData(true, it) }, this::processErrors)
                    .addToDisposables()
        }
    }

    private fun loadWithPaginator() {
        destroyPaginator()

        paginator = PageOffsetPaginator(getPaginatorRequestFactory(), viewController)
        paginator?.refresh()
    }

    fun loadNextPage() {
        if (isPagination()) paginator?.loadNewPage()
    }

    fun onRefresh() {
        loadData()
    }

    private fun getPaginatorRequestFactory(): (Int) -> Single<List<SearchItem>> {
        return when (type) {
            Type.ANIME -> { page: Int -> interactor.loadAnimeListWithFilters(filters, page).map(converter) }
            Type.MANGA -> { page: Int -> interactor.loadMangaListWithFilters(filters, page).map(converter) }
            else -> { page: Int -> interactor.loadRanobeListWithFilters(filters, page).map(converter) }
        }
    }

    private fun getSimpleRequestFactory(): Single<List<SearchItem>> {
        return when (type) {
            Type.CHARACTER -> interactor.loadCharacterListWithFilters(filters).map(converter)
            else -> interactor.loadPersonListWithFilters(filters).map(converter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyPaginator()
    }

    private val viewController = object : ViewController<SearchItem> {
        override fun showData(show: Boolean, data: List<SearchItem>) {
            if (show) viewState.showData(data)
            else viewState.hideData()
        }

        override fun showEmptyView(show: Boolean) {
            if (show) viewState.showEmptyView()
            else viewState.hideEmptyView()
        }

        override fun showRefreshProgress(show: Boolean) {
            if (show) viewState.onShowLoading()
            else viewState.onHideLoading()
        }

        override fun showEmptyProgress(show: Boolean) {
            if (show) viewState.onShowLoading()
            else viewState.onHideLoading()
        }

        override fun showPageProgress(show: Boolean) {
            if (show) viewState.showPageLoading()
            else viewState.hidePageLoading()
        }

        override fun showError(throwable: Throwable) {
            processErrors(throwable)
        }

        override fun showEmptyError(show: Boolean, throwable: Throwable?) {
            if (show) viewState.showEmptyView()
            else viewState.hideEmptyView()

            throwable?.let { processErrors(it) }
        }
    }

    private fun isPagination(): Boolean = supportsPagination.contains(type)

    private fun destroyPaginator() {
        paginator?.release()
        paginator = null
    }

    fun onTypeChanged(newTypePos: Int) {
        this.type = getType(newTypePos)

        viewState.apply {
            selectType(newTypePos)
            showData(emptyList())
            hideEmptyView()
            setDefaultEmptyText()
        }

        filters.clear()
        onRefresh()
    }

    private fun getType(pos: Int): Type {
        return when (pos) {
            0 -> Type.ANIME
            1 -> Type.MANGA
            2 -> Type.RANOBE
            3 -> Type.CHARACTER
            4 -> Type.PERSON
            else -> Type.ANIME
        }
    }

}