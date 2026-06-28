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
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentCalendarBinding
import com.gnoemes.shikimori.databinding.LayoutDefaultPlaceholdersBinding
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
import javax.inject.Inject

class CalendarFragment : BaseFragment<CalendarPresenter, CalendarView>(), CalendarView {

    private var _binding: FragmentCalendarBinding? = null
    private val binding: FragmentCalendarBinding? get() = _binding
    private var _fragmentPlaceholdersBinding: LayoutDefaultPlaceholdersBinding? = null
    private val fragmentPlaceholdersBinding: LayoutDefaultPlaceholdersBinding? get() = _fragmentPlaceholdersBinding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        _fragmentPlaceholdersBinding = LayoutDefaultPlaceholdersBinding.bind(_binding!!.coordinator)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = _binding ?: return
        val pb = _fragmentPlaceholdersBinding

        b.toolbar.setNavigationIcon(R.drawable.ic_search)

        with(b.includedLayoutDefaultList.recyclerView) {
            adapter = this@CalendarFragment.adapter
            layoutManager = LinearLayoutManager(context).apply { initialPrefetchItemCount = 3 }
            val customSpacing = context!!.dp(84)
            addItemDecoration(VerticalSpaceItemDecorator(context!!.dp(20), true, firstCustomSpacing = customSpacing, lastCustomSpacing = context!!.dp(20)))
            setHasFixedSize(true)
        }

        b.includedLayoutDefaultList.refreshLayout.layoutParams = (b.includedLayoutDefaultList.refreshLayout.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
            behavior = OverlapHeaderScrollingBehavior()
        }
        b.includedLayoutDefaultList.refreshLayout.setProgressViewOffset(false, context!!.dp(24), context!!.dp(96))

        pb?.networkErrorView?.apply {
            setText(R.string.common_error_message_without_pull)
            callback = { getPresenter().initData() }
            showButton()
        }

        with(b.searchView) {
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
            findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)?.apply {
                setPadding(0, 0, context.dp(8), 0)
                setHintTextColor(context.colorStateList(context.attr(R.attr.colorOnPrimarySecondary).resourceId))
            }
            findViewById<LinearLayout>(androidx.appcompat.R.id.search_edit_frame)?.apply {
                layoutParams = (layoutParams as? LinearLayout.LayoutParams)?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        marginStart = 0
                    }; leftMargin = 0
                }
            }
            findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)?.apply {
                setPadding(context!!.dp(12), 0, context!!.dp(12), 0)
                tint(context.colorAttr(R.attr.colorOnPrimary))
            }
        }

        b.includedLayoutDefaultList.refreshLayout.setOnRefreshListener { calendarPresenter.onRefresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _fragmentPlaceholdersBinding = null
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

    override fun showContent(show: Boolean) { _binding?.includedLayoutDefaultList?.recyclerView?.visibleIf { show } }
    override fun onShowLoading() { _binding?.includedLayoutDefaultList?.refreshLayout?.showRefresh() }
    override fun onHideLoading() { _binding?.includedLayoutDefaultList?.refreshLayout?.hideRefresh() }

    override fun showEmptyView() {
        val item = SeriesPlaceholderItem(R.string.calendar_empty_title, R.string.calendar_empty_description)
        adapter.bindItems(mutableListOf(item))
    }

    override fun showNetworkView() {
        _binding?.placeholderOverlay?.visible()
        _fragmentPlaceholdersBinding?.networkErrorView?.visible()
    }

    override fun hideNetworkView() {
        _binding?.placeholderOverlay?.gone()
    }

    override fun hideEmptyView() { _fragmentPlaceholdersBinding?.emptyContentView?.gone() }
}
