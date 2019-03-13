package com.gnoemes.shikimori.presentation.presenter.search

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.search.SearchInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.SearchConstants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import com.gnoemes.shikimori.entity.search.presentation.SearchPayload
import com.gnoemes.shikimori.presentation.presenter.base.BasePaginationPresenter
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
) : BasePaginationPresenter<SearchItem, SearchView>() {

    var searchPayload: SearchPayload? = null

    var type: Type = Type.ANIME
    var filters = HashMap<String, MutableList<FilterItem>>()

    private val supportsPagination = listOf(Type.ANIME, Type.MANGA, Type.RANOBE)

    override fun initData() {
        if (searchPayload != null) {
            onPayloadSearch()
        }

        loadData()

        viewState.setDefaultEmptyText()
    }

    override fun loadData() {
        if (isPagination()) loadWithPaginator()
        else loadSimple()
    }

    private fun loadSimple() {
        if (filters.isEmpty()) {
            viewState.apply { showContent(false); setSimpleEmptyText(); showEmptyView(); onHideLoading() }
        } else {
            getSimpleRequestFactory()
                    .appendLoadingLogic(viewState)
                    .subscribe({ showData(true, it) }, this::processErrors)
                    .addToDisposables()
        }
    }

    private fun loadWithPaginator() {
        super.loadData()
    }

    fun onFilterClicked() {
        viewState.showFilter(type, filters)
        logEvent(AnalyticEvent.SEARCH_FILTER_OPENED)
    }

    override fun loadNextPage() {
        if (isPagination()) super.loadNextPage()
    }

    override fun getPaginatorRequestFactory(): (Int) -> Single<List<SearchItem>> {
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

    private fun isPagination(): Boolean = supportsPagination.contains(type)

    fun onTypeChanged(newTypePos: Int) {
        this.type = getType(newTypePos)

        destroyPaginator()

        viewState.apply {
            selectType(newTypePos)
            showData(emptyList())
            showContent(true)
            hideEmptyView()
            setDefaultEmptyText()
            if (supportsPagination.contains(type)) showFilterButton()
            else hideFilterButton()
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

    fun onFilterSelected(appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        filters = appliedFilters
        onRefresh()
        viewState.updateFilterIcon(appliedFilters.isEmpty())
    }

    private fun onPayloadSearch() {
        searchPayload?.let {
            if (it.genre != null) onGenreSearch(it.genre)
            else if (it.studioId != null) onStudioSearch(it.studioId)
        }
        viewState.addBackButton()
    }

    fun onQuerySearch(query: String?) {
        val filterItem = FilterItem(SearchConstants.SEARCH, query, null)
        putFilter(filterItem)
        onRefresh()
    }

    private fun onStudioSearch(studioId: Long) {
        val filterItem = FilterItem(SearchConstants.STUDIO, studioId.toString(), null)
        putFilter(filterItem)
    }

    private fun onGenreSearch(genre: Genre) {
        val id = if (type == Type.ANIME) genre.animeId else genre.mangaId
        val filterItem = FilterItem(SearchConstants.GENRE, id, genre.russianName)
        putFilter(filterItem)
    }

    private fun putFilter(filter: FilterItem) {
        putFilter(filter.action, filter)
    }

    private fun putFilter(key: String, filter: FilterItem) {
        filters[key] = mutableListOf(filter)
    }
}