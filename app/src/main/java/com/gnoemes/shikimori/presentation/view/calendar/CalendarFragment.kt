package com.gnoemes.shikimori.presentation.view.calendar

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarPage
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.presenter.calendar.CalendarPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.calendar.adapter.CalendarAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.PageTitleAdapter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.visible
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.layout_appbar_tabs.*
import kotlinx.android.synthetic.main.layout_default_list.view.*
import kotlinx.android.synthetic.main.layout_default_placeholders.view.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class CalendarFragment : BaseFragment<CalendarPresenter, CalendarView>(), CalendarView {

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var calendarPresenter: CalendarPresenter

    @ProvidePresenter
    fun providePresenter(): CalendarPresenter {
        calendarPresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            calendarPresenter.localRouter = (it as RouterProvider).localRouter
        }

        return calendarPresenter
    }

    companion object {
        fun newInstance() = CalendarFragment()
    }

    private lateinit var pages: List<Pair<String, View>>

    private val ongoingsAdapter by lazy { CalendarAdapter(imageLoader) { getPresenter().onAnimeClicked(it) } }
    private val myOngoingsAdapter by lazy { CalendarAdapter(imageLoader) { getPresenter().onAnimeClicked(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        if (savedInstanceState != null) {
            savedInstanceState.getBundle("ongoingsState")?.let { ongoingsAdapter.onRestoreInstanceState(it) }
            savedInstanceState.getBundle("myOngoingsState")?.let { myOngoingsAdapter.onRestoreInstanceState(it) }
        }
    }

    private fun initViews() {
        toolbar?.gone()
        progressBar?.gone()
        val ongoingsPage = layoutInflater?.inflate(R.layout.page_calendar, null)!!
        val myOngoingsPage = layoutInflater?.inflate(R.layout.page_calendar, null)!!

        pages = listOf(
                Pair(getString(R.string.common_all), ongoingsPage),
                Pair(getString(R.string.calendar_my_ongoings), myOngoingsPage)
        )

        pagesContainerView.offscreenPageLimit = 2
        pagesContainerView.adapter = PageTitleAdapter(pages)

        tabLayout.setupWithViewPager(pagesContainerView)

        fun initRecycler(page: CalendarPage) {
            val isAll = page == CalendarPage.ALL
            val pageLayout = if (isAll) ongoingsPage else myOngoingsPage
            val pageAdapter = if (isAll) ongoingsAdapter else myOngoingsAdapter

            with(pageLayout.recyclerView) {
                adapter = pageAdapter.apply { if (!hasObservers()) setHasStableIds(true) }
                layoutManager = LinearLayoutManager(context).apply { initialPrefetchItemCount = 3 }
                setHasFixedSize(true)
                setItemViewCacheSize(20)
            }
        }

        initRecycler(CalendarPage.ALL)
        initRecycler(CalendarPage.MY_ONGOINGS)

        ongoingsPage.refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }
        myOngoingsPage.refreshLayout.setOnRefreshListener { getPresenter().onRefreshMyOngoings() }

        ongoingsPage.networkErrorView.setText(R.string.common_error_message)
        myOngoingsPage.emptyContentView.setText(R.string.calendar_nothing)
        myOngoingsPage.networkErrorView.setText(R.string.common_error_message)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val ongoingsState = ongoingsAdapter.onSaveInstanceState()
        outState.putBundle("ongoingsState", ongoingsState)
        val myOngoingsState = myOngoingsAdapter.onSaveInstanceState()
        outState.putBundle("myOngoingsState", myOngoingsState)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): CalendarPresenter = calendarPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_calendar

    private fun getPage(calendarPage: CalendarPage): View = pages[calendarPage.ordinal].second

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showOngoings(calendarPage: CalendarPage, items: List<CalendarViewModel>) {
        getPage(calendarPage).recyclerView.apply {
            visible()
            (adapter as? CalendarAdapter)?.bindItems(items)
        }
    }

    override fun hideOngoings(calendarPage: CalendarPage) {
        getPage(calendarPage).recyclerView.gone()
    }

    override fun onShowOngoingsLoading(calendarPage: CalendarPage) {
        getPage(calendarPage).refreshLayout.isRefreshing = true
    }

    override fun onHideOngoingsLoading(calendarPage: CalendarPage) {
        getPage(calendarPage).refreshLayout.isRefreshing = false
    }

    override fun onShowNetworkError(calendarPage: CalendarPage) {
        getPage(calendarPage).networkErrorView.visible()
    }

    override fun onHideNetworkError(calendarPage: CalendarPage) {
        getPage(calendarPage).networkErrorView.gone()
    }

    override fun onShowEmptyView(calendarPage: CalendarPage) {
        getPage(calendarPage).emptyContentView.visible()
    }

    override fun onHideEmptyView(calendarPage: CalendarPage) {
        getPage(calendarPage).emptyContentView.gone()
    }
}