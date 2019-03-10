package com.gnoemes.shikimori.presentation.view.search

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import com.gnoemes.shikimori.entity.search.presentation.SearchNavigationData
import com.gnoemes.shikimori.presentation.presenter.search.SearchPresenter
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.search.adapter.SearchAdapter
import com.gnoemes.shikimori.presentation.view.search.filter.FilterCallback
import com.gnoemes.shikimori.presentation.view.search.filter.FilterFragment
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.GridItemDecorator
import com.lapism.searchview.SearchView.VERSION_MARGINS_MENU_ITEM
import com.lapism.searchview.SearchView.VERSION_MENU_ITEM
import com.santalu.widget.ReSpinner
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class SearchFragment : BasePaginationFragment<SearchItem, SearchPresenter, SearchView>(), SearchView, FilterCallback, HasSupportFragmentInjector {

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    @ProvidePresenter
    fun providePresenter(): SearchPresenter {
        searchPresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            searchPresenter.localRouter = (parentFragment as RouterProvider).localRouter
        }

        arguments.ifNotNull {
            val data = it.getParcelable<SearchNavigationData>(AppExtras.ARGUMENT_SEARCH_DATA)
            data?.let {
                searchPresenter.searchPayload = data.payload
                searchPresenter.type = data.type
            }
        }

        return searchPresenter
    }

    companion object {
        fun newInstance(data: SearchNavigationData?) = SearchFragment().withArgs { putParcelable(AppExtras.ARGUMENT_SEARCH_DATA, data) }
    }

    private var spinner: ReSpinner? = null
    private var searchView: com.lapism.searchview.SearchView? = null

    private val searchAdapter by lazy { SearchAdapter(imageLoader, getPresenter()::onContentClicked).apply { if (!hasObservers()) setHasStableIds(true) } }

    override val adapter: BasePaginationAdapter
        get() = searchAdapter

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
        spinner?.background = spinner?.background?.apply { tint(context!!.colorAttr(R.attr.colorOnPrimary)) }

        searchView = com.lapism.searchview.SearchView(context)
                .apply {
                    setNavigationIcon(context.themeDrawable(R.drawable.ic_arrow_back, R.attr.colorOnPrimary))
                    setHint(R.string.common_search)
                    setShadow(false)
                    setVoice(false)
                    version = VERSION_MENU_ITEM
                    setVersionMargins(VERSION_MARGINS_MENU_ITEM)
                    shouldHideOnKeyboardClose = true
                    shouldClearOnClose = true
                    setOnOpenCloseListener(searchViewOpenListener)
                    setOnQueryTextListener(searchViewQueryListener)
                }

        toolbar?.apply {
            title = null
            addView(spinner)
            addView(searchView)
            inflateMenu(R.menu.menu_search)
            setOnMenuItemClickListener {
                searchView?.open(true, it)
                true
            }
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

    }

    private val searchViewOpenListener = object : com.lapism.searchview.SearchView.OnOpenCloseListener {
        override fun onOpen(): Boolean {
            spinner?.gone()
            toolbar?.menu?.getItem(0)?.isVisible = false
            return false
        }

        override fun onClose(): Boolean {
            spinner?.visible()
            toolbar?.menu?.getItem(0)?.isVisible = true
            return false
        }
    }

    private val searchViewQueryListener = object : com.lapism.searchview.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            getPresenter().onQuerySearch(query)
            searchView?.close(true)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): SearchPresenter = searchPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_search

    ///////////////////////////////////////////////////////////////////////////
    // CALLBACKS
    ///////////////////////////////////////////////////////////////////////////

    override fun onFiltersSelected(tag : String?, appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        getPresenter().onFilterSelected(appliedFilters)
    }

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showFilter(type: Type, filters: HashMap<String, MutableList<FilterItem>>) {
        val tag = "filterDialog"
        val fragment = fragmentManager?.findFragmentByTag(tag)
        if (fragment == null) {
            val filter = FilterFragment.newInstance(type, filters)
            filter.setTargetFragment(this, 42)
            postViewAction { filter.show(fragmentManager!!, tag) }
        }
    }

    override fun selectType(newTypePos: Int) {
        spinner?.setSelection(newTypePos, false)
    }

    override fun addBackButton() {
        toolbar?.addBackButton { getPresenter().onBackPressed() }
    }

    override fun showFilterButton() = fab.show()
    override fun hideFilterButton() = fab.hide()
    override fun setSimpleEmptyText() = emptyContentView.setText(R.string.search_need_query)
    override fun setDefaultEmptyText() = emptyContentView.setText(R.string.search_nothing)
}