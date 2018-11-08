package com.gnoemes.shikimori.presentation.view.calendar

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.presenter.calendar.CalendarPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.calendar.adapter.CalendarAdapter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.visible
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
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

    private val adapter by lazy { CalendarAdapter(imageLoader) { getPresenter().onAnimeClicked(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        progressBar?.gone()
        with(recyclerView) {
            adapter = this@CalendarFragment.adapter
            layoutManager = LinearLayoutManager(context).apply { initialPrefetchItemCount = 3 }
            setHasFixedSize(true)
            setItemViewCacheSize(7)
        }

        toolbar.apply {
            setTitle(R.string.common_calendar)
            inflateMenu(R.menu.menu_calendar)
            setOnMenuItemClickListener {
                getPresenter().onSwitchFilter()
                true
            }
        }


        refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }
        emptyContentView.setText(R.string.calendar_nothing)
        networkErrorView.setText(R.string.common_error_message)
    }


    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): CalendarPresenter = calendarPresenter

    override fun getFragmentLayout(): Int = R.layout.layout_default_list

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showItems(items: List<CalendarViewModel>) {
        recyclerView.visible()
        adapter.bindItems(items)
    }

    override fun hideList() = recyclerView.gone()

    override fun onShowLoading() {
        refreshLayout.isRefreshing = true
    }

    override fun onHideLoading() {
        refreshLayout.isRefreshing = false
    }

    override fun showNetworkView(block: Boolean) {
        super<BaseFragment>.showNetworkView(false)
        recyclerView.gone()
    }

    override fun hideNetworkView() {
        super<BaseFragment>.hideNetworkView()
        recyclerView.visible()
    }
}