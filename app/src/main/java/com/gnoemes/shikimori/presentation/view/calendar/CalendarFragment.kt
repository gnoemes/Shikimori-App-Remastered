package com.gnoemes.shikimori.presentation.view.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.entity.series.presentation.SeriesPlaceholderItem
import com.gnoemes.shikimori.presentation.presenter.calendar.CalendarPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.calendar.adapter.CalendarAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.OverlapHeaderScrollingBehavior
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import javax.inject.Inject

class CalendarFragment : BaseFragment<CalendarPresenter, CalendarView>(), CalendarView {

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var calendarPresenter: CalendarPresenter

    @ProvidePresenter
    fun providePresenter(): CalendarPresenter = presenterProvider.get().apply {
        localRouter = (parentFragment as RouterProvider).localRouter
    }

    companion object {
        fun newInstance() = CalendarFragment()
    }

    private val adapter by lazy { CalendarAdapter(imageLoader, getPresenter()::onAnimeClicked) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(getFragmentLayout(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationIcon(R.drawable.ic_search)

        with(recyclerView) {
            adapter = this@CalendarFragment.adapter
            layoutManager = LinearLayoutManager(context).apply { initialPrefetchItemCount = 3 }
            val customSpacing = context.dp(84)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(20), true, firstCustomSpacing = customSpacing, lastCustomSpacing = context!!.dp(20)))
            setHasFixedSize(true)
        }

        refreshLayout.layoutParams = (refreshLayout.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
            behavior = OverlapHeaderScrollingBehavior()
        }
        refreshLayout.setProgressViewOffset(false, context!!.dp(24), context!!.dp(96))

        networkErrorView.apply {
            setText(R.string.common_error_message_without_pull)
            callback = { getPresenter().initData() }
            showButton()
        }

        with(searchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideSoftInput()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    getPresenter().onQueryChanged(newText)
                    return true
                }
            })
            findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)?.apply {
                setPadding(0, 0, context.dp(8), 0)
                setHintTextColor(context.colorStateList(context.attr(R.attr.colorOnPrimarySecondary).resourceId))
            }
            findViewById<LinearLayout>(R.id.search_edit_frame)?.apply {
                layoutParams = (layoutParams as? LinearLayout.LayoutParams)?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        marginStart = 0
                    }; leftMargin = 0
                }
            }
            findViewById<ImageView>(R.id.search_close_btn)?.apply {
                setPadding(context!!.dp(12), 0, context!!.dp(12), 0)
                tint(context.colorAttr(R.attr.colorOnPrimary))
            }
        }

        refreshLayout.setOnRefreshListener { calendarPresenter.onRefresh() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): CalendarPresenter = calendarPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_calendar

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(items: List<CalendarViewModel>) {
        adapter.bindItems(items)
    }

    override fun showContent(show: Boolean) = recyclerView.visibleIf { show }
    override fun onShowLoading() = refreshLayout.showRefresh()
    override fun onHideLoading() = refreshLayout.hideRefresh()

    override fun showEmptyView() {
        val item = SeriesPlaceholderItem(R.string.calendar_empty_title, R.string.calendar_empty_description)
        adapter.bindItems(mutableListOf(item))
    }
}