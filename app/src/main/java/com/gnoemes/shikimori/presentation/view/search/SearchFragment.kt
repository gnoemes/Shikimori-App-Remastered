package com.gnoemes.shikimori.presentation.view.search

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import com.gnoemes.shikimori.entity.search.presentation.SearchNavigationData
import com.gnoemes.shikimori.presentation.presenter.search.SearchPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.search.adapter.SearchAdapter
import com.gnoemes.shikimori.presentation.view.search.filter.FilterCallback
import com.gnoemes.shikimori.presentation.view.search.filter.FilterDialogFragment
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.GridItemDecorator
import com.santalu.widget.ReSpinner
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class SearchFragment : BaseFragment<SearchPresenter, SearchView>(), SearchView, FilterCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter(): SearchPresenter {
        searchPresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            searchPresenter.localRouter = (parentFragment as RouterProvider).localRouter
        }

        arguments.ifNotNull {
            val data = it.getParcelable<SearchNavigationData>(AppExtras.ARGUMENT_SEARCH_DATA)
            data?.let {
                searchPresenter.genre = data.genre
                searchPresenter.type = data.type
            }
        }

        return searchPresenter
    }

    companion object {
        fun newInstance(data: SearchNavigationData?) = SearchFragment().withArgs { putParcelable(AppExtras.ARGUMENT_SEARCH_DATA, data) }
    }

    private var spinner: ReSpinner? = null

    private val adapter by lazy { SearchAdapter(imageLoader, getPresenter()::onContentClicked).apply { if (!hasObservers()) setHasStableIds(true) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar?.gone()

        spinner = ReSpinner(view.context)
        spinner?.adapter = ArrayAdapter<String>(
                view.context,
                R.layout.item_spinner_toolbar_normal,
                resources.getStringArray(R.array.search_types
                ))
        spinner?.setOnItemClickListener { _, _, position, _ -> getPresenter().onTypeChanged(position) }

        toolbar?.apply {
            title = null
            addView(spinner)
        }

        with(recyclerView) {
            val spanCount = context.calculateColumns(R.dimen.image_search_width)
            adapter = this@SearchFragment.adapter
            layoutManager = GridLayoutManager(context, spanCount)
            setHasFixedSize(true)
            addItemDecoration(GridItemDecorator(context.dimen(R.dimen.margin_small).toInt()))
            addOnScrollListener(nextPageListener)
        }

        fab.setOnClickListener { getPresenter().onFilterClicked() }

        refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }
    }

    private val nextPageListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val manager = (recyclerView.layoutManager as GridLayoutManager)
            val visibleItemPosition = manager.findLastCompletelyVisibleItemPosition() + Constants.DEFAULT_LIMIT / 2
            val itemCount = manager.itemCount

            if (!adapter.isProgress() && visibleItemPosition >= itemCount) {
                getPresenter().loadNextPage()
            }
        }
    }

    override fun onDestroyView() {
        recyclerView.removeOnScrollListener(nextPageListener)
        super.onDestroyView()
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): SearchPresenter = searchPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_search

    ///////////////////////////////////////////////////////////////////////////
    // CALLBACKS
    ///////////////////////////////////////////////////////////////////////////
    override fun onFiltersSelected(appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        getPresenter().onFilterSelected(appliedFilters)
    }

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(data: List<SearchItem>) {
        adapter.bindItems(data)
        recyclerView.visible()
    }

    override fun hideData() {
        recyclerView.gone()
    }

    override fun showFilter(type: Type, filters: HashMap<String, MutableList<FilterItem>>) {
        val tag = "filterDialog"
        val fragment = fragmentManager?.findFragmentByTag(tag)
        if (fragment == null) {
            val filter = FilterDialogFragment.newInstance(type, filters)
            filter.setTargetFragment(this, 42)
            postViewAction { filter.show(fragmentManager, tag) }
        }
    }

    override fun selectType(newTypePos: Int) {
        spinner?.setSelection(newTypePos, false)
    }

    override fun showFilterButton() = fab.show()
    override fun hideFilterButton() = fab.hide()
    override fun onShowLoading() = refreshLayout.showRefresh()
    override fun onHideLoading() = refreshLayout.hideRefresh()
    override fun showPageLoading() = postViewAction { adapter.showProgress(true) }
    override fun hidePageLoading() = postViewAction { adapter.showProgress(false) }
    override fun setSimpleEmptyText() = emptyContentView.setText(R.string.search_need_query)
    override fun setDefaultEmptyText() = emptyContentView.setText(R.string.search_nothing)
}