package com.gnoemes.shikimori.presentation.view.rates

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentRateBinding
import com.gnoemes.shikimori.databinding.LayoutDefaultPlaceholdersBinding
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.rates.presentation.RateCategory
import com.gnoemes.shikimori.entity.rates.presentation.RateNavigationData
import com.gnoemes.shikimori.entity.series.presentation.SeriesPlaceholderItem
import com.gnoemes.shikimori.presentation.presenter.rates.RatePresenter
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.base.fragment.TabRootFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.EditRateFragment
import com.gnoemes.shikimori.presentation.view.rates.adapter.RateAdapter
import com.gnoemes.shikimori.presentation.view.rates.adapter.RateItemTouchHelperCallback
import com.gnoemes.shikimori.presentation.view.rates.sort.RateSortDialog
import com.gnoemes.shikimori.presentation.view.rates.status.RateStatusDialog
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.OverlapHeaderScrollingBehavior
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class RateFragment : BasePaginationFragment<Rate, RatePresenter, RateView>(), RateView, EditRateFragment.RateDialogCallback, RateSortDialog.RateSortCallback, TabRootFragment, RateStatusDialog.RateStatusCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var settingsSource: SettingsSource

    @InjectPresenter
    lateinit var ratePresenter: RatePresenter

    @ProvidePresenter
    fun providePresenter(): RatePresenter = presenterProvider.get().apply {
        localRouter = (parentFragment as RouterProvider).localRouter
        val data = arguments?.getParcelable<RateNavigationData>(AppExtras.ARGUMENT_RATE_DATA)
        data?.let {
            userId = it.userId
            type = it.type
            priorityStatus = it.status
        }
    }

    private val _adapter by lazy { RateAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onListAction, getPresenter()::onAction, getPresenter()::onSortAction) }

    override val adapter: BasePaginationAdapter
        get() = _adapter

    private var _binding: FragmentRateBinding? = null
    private val binding: FragmentRateBinding? get() = _binding
    private var _fragmentPlaceholdersBinding: LayoutDefaultPlaceholdersBinding? = null
    private val fragmentPlaceholdersBinding: LayoutDefaultPlaceholdersBinding? get() = _fragmentPlaceholdersBinding

    companion object {
        private const val DRAWER_KEY = "DRAWER_KEY"
        fun newInstance(data: RateNavigationData?) = RateFragment().withArgs {
            putParcelable(AppExtras.ARGUMENT_RATE_DATA, data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        _fragmentPlaceholdersBinding = LayoutDefaultPlaceholdersBinding.bind(_binding!!.coordinator)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = _binding ?: return
        val pb = _fragmentPlaceholdersBinding

        b.toolbar.apply {
            title = null
            setNavigationOnClickListener { toggleDrawer() }
            inflateMenu(R.menu.menu_rates)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_switch_list -> getPresenter().onChangeType()
                }
                true
            }
        }

        with(b.searchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    b.toolbar.menuVisibleIf { query.isNullOrEmpty() }
                    hideSoftInput()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    b.toolbar.menuVisibleIf { newText.isNullOrEmpty() }
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

        savedInstanceState?.let {
            b.drawer.post {
                if (it.getBoolean(DRAWER_KEY)) openDrawer()
                else closeDrawer()
            }
        }

        val rvContext = b.includedLayoutDefaultList.recyclerView.context
        with(b.includedLayoutDefaultList.recyclerView) {
            adapter = this@RateFragment.adapter
            layoutManager = LinearLayoutManager(rvContext).apply { initialPrefetchItemCount = 5 }
            itemAnimator = DefaultItemAnimator()
            val customSpacing = rvContext.dp(68)
            addItemDecoration(VerticalSpaceItemDecorator(rvContext.dp(8), true, firstCustomSpacing = customSpacing, lastCustomSpacing = rvContext.dp(16)))
            addOnScrollListener(nextPageListener)
            val touchHelper = ItemTouchHelper(RateItemTouchHelperCallback(this@RateFragment._adapter, settingsSource))
            touchHelper.attachToRecyclerView(this)
            setHasFixedSize(true)
        }

        b.includedLayoutDefaultList.refreshLayout.layoutParams = (b.includedLayoutDefaultList.refreshLayout.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
            behavior = OverlapHeaderScrollingBehavior()
        }
        b.includedLayoutDefaultList.refreshLayout.setProgressViewOffset(false, context!!.dp(24), context!!.dp(96))

        initDrawer()
        initNav()

        pb?.emptyContentView?.setText(R.string.rate_empty)
        pb?.networkErrorView?.apply {
            setText(R.string.common_error_message_without_pull)
            callback = { getPresenter().initData() }
            showButton()
        }
        b.rateEmptyView.root.gone()
        progressBinding?.progressBar?.gone()

        b.authLayout.signInBtn.onClick { getPresenter().onSignIn() }
        b.authLayout.signUpBtn.onClick { getPresenter().onSignUp() }

        b.rateEmptyView.rateTitleView.setText(R.string.rate_empty_list)
        b.authLayout.descriptionView.gone()
        b.authLayout.root.gone()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // View may be destroyed if fragment is in background (e.g., AuthActivity launched on top)
        val b = _binding ?: return
        outState.putBoolean(DRAWER_KEY, b.drawer?.isDrawerOpen(GravityCompat.START) ?: false)
    }

    private fun initDrawer() {
        val b = _binding ?: return
        b.drawer.apply {
            val toggle = ActionBarDrawerToggle(
                    activity, b.drawer, b.toolbar, R.string.rate_drawer_open, R.string.rate_drawer_close)

            addDrawerListener(toggle)
            setViewScale(Gravity.START, 0.9f)
            setRadius(Gravity.START, 35f)
            setViewElevation(Gravity.START, 20f)
            toggle.syncState()
        }
    }

    private fun initNav() {
        val b = _binding ?: return
        updateNavColors(RateStatus.WATCHING.ordinal)
        b.navView.setNavigationItemSelectedListener {
            getPresenter().onChangeStatus(RateStatus.values()[it.itemId])
            b.navView.menu.iterator().forEach { item -> item.actionView?.isSelected = false }
            it.actionView?.isSelected = true
            true
        }

        val menuContainer = b.navView.findViewById<NavigationMenuView>(com.google.android.material.R.id.design_navigation_view)
        menuContainer.layoutParams = FrameLayout.LayoutParams(menuContainer.layoutParams).apply { height = ViewGroup.LayoutParams.WRAP_CONTENT; gravity = Gravity.CENTER_VERTICAL }
    }

    private fun toggleDrawer() {
        val b = _binding ?: return
        if (b.drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            openDrawer()
        }
    }

    private fun openDrawer() {
        _binding?.drawer?.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer() {
        _binding?.drawer?.closeDrawer(GravityCompat.START)
    }

    private fun getRateTextColor(ordinal: Int): Int = when (ordinal) {
        RateStatus.COMPLETED.ordinal -> R.color.selector_rate_item_menu_text_color_green
        RateStatus.ON_HOLD.ordinal -> R.color.selector_rate_item_menu_text_color_gray
        RateStatus.DROPPED.ordinal -> R.color.selector_rate_item_menu_text_color_red
        else -> R.color.selector_rate_item_menu_text_color_blue
    }

    private fun getRateBackground(ordinal: Int): Int = when (ordinal) {
        RateStatus.COMPLETED.ordinal -> R.drawable.selector_rate_item_menu_background_watched
        RateStatus.ON_HOLD.ordinal -> R.drawable.selector_rate_item_menu_background_on_hold
        RateStatus.DROPPED.ordinal -> R.drawable.selector_rate_item_menu_background_dropped
        else -> R.drawable.selector_rate_item_menu_background_default
    }

    private fun getActionTextView(ordinal: Int, count: Int, isSelected: Boolean): TextView {
        val view = TextView(context!!)
        view.text = "$count"
        view.gravity = Gravity.CENTER_VERTICAL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTextColor(context!!.colorStateList(getRateTextColor(ordinal)))
        }
        view.isSelected = isSelected
        return view
    }

    private fun updateNavColors(rateIndex: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            _binding?.navView?.apply {
                setItemBackgroundResource(getRateBackground(rateIndex))
                itemTextColor = context.colorStateList(getRateTextColor(rateIndex))
            }
        }
    }

    override fun onTabRootAction() {
        getPresenter().onOpenRandom()
    }

    override fun onUpdateRate(rate: UserRate) {
        getPresenter().onUpdateRate(rate)
    }

    override fun onDeleteRate(id: Long) {
        getPresenter().onDeleteRate(id)
    }

    override fun onStatusChanged(id: Long, newStatus: RateStatus) {
        getPresenter().onChangeRateStatus(id, newStatus)
    }

    override fun onDestroyView() {
        _binding?.includedLayoutDefaultList?.recyclerView?.adapter = null
        super.onDestroyView()
        _binding = null
    }

    override fun onSortClicked(sort: RateSort) {
        getPresenter().onSortChanged(sort)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): RatePresenter = ratePresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_rate

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(data: List<Any>) {
        //fix auto scroll on sort
        closeDrawer()
        val b = _binding ?: return
        val parcelable = b.includedLayoutDefaultList.recyclerView.layoutManager?.onSaveInstanceState()
        adapter.bindItems(data)
        b.includedLayoutDefaultList.recyclerView.visible()
        b.includedLayoutDefaultList.recyclerView.layoutManager?.onRestoreInstanceState(parcelable)
    }

    override fun scrollToTop() {
        _binding?.appBarLayout?.setExpanded(true)
        _binding?.includedLayoutDefaultList?.recyclerView?.scrollToPosition(0)
    }

    override fun showRateDialog(title: String, userRate: UserRate) {
        hideSoftInput()
        val dialog = EditRateFragment.newInstance(rate = userRate, isAnime = userRate.targetType == Type.ANIME, title = title)
        dialog.show(childFragmentManager, "RateTag")
    }

    override fun showSortDialog(sorts: List<Triple<RateSort, String, Boolean>>) {
        hideSoftInput()
        val dialog = RateSortDialog.newInstance(sorts)
        dialog.show(childFragmentManager, "SortDialog")
    }

    override fun showStatusDialog(id: Long, name: String, currentStatus: RateStatus, isAnime: Boolean) {
        hideSoftInput()
        val dialog = RateStatusDialog.newInstance(id, name, currentStatus, isAnime)
        dialog.show(childFragmentManager, "StatusDialog")
    }

    override fun onBackPressed() {
        val b = _binding
        if (b?.drawer?.isDrawerOpen(GravityCompat.START) == true) closeDrawer()
        else super.onBackPressed()
    }

    override fun setNavigationItems(items: List<RateCategory>) {
        val b = _binding ?: return
        b.navView.menu.apply {
            val checkedId = b.navView.checkedItem?.itemId ?: 0
            clear()
            items.forEach {
                add(0, it.status.ordinal, it.status.ordinal, it.localizedCategory)
                findItem(it.status.ordinal)?.actionView = getActionTextView(it.status.ordinal, it.count, checkedId == it.status.ordinal)
            }
            if (items.isEmpty()) {
                add(R.string.rate_empty)
            }

            setGroupCheckable(0, true, true)
            b.navView.setCheckedItem(checkedId)
        }
    }

    override fun selectRateStatus(rateStatus: RateStatus) {
        val b = _binding ?: return
        b.navView.apply {
            updateNavColors(rateStatus.ordinal)
            setCheckedItem(rateStatus.ordinal)
            (menu.findItem(rateStatus.ordinal)?.actionView as? TextView)?.isSelected = true
        }
    }

    override fun selectType(type: Type) {
        _binding?.searchView?.let {
            val hint = if (type == Type.ANIME) it.context.getString(R.string.common_anime)
            else it.context.getString(R.string.common_manga_and_ranobe)
            it.queryHint = hint
        }
    }

    override fun showContent(show: Boolean) {
        _binding?.includedLayoutDefaultList?.recyclerView?.visibleIf { show }
        if (show) _fragmentPlaceholdersBinding?.emptyContentView?.gone()
    }

    override fun onShowLoading() { _binding?.includedLayoutDefaultList?.refreshLayout?.showRefresh() }

    override fun onHideLoading() { _binding?.includedLayoutDefaultList?.refreshLayout?.hideRefresh() }

    override fun showEmptySearchView(it: List<Any>) {
        //TODO create common placeholder
        val item = SeriesPlaceholderItem(R.string.rate_search_empty_title, R.string.rate_search_empty_message)
        val items = it.toMutableList()
        items.add(item)
        showData(items)
    }

    override fun showPinLimitMessage() {
        Toast.makeText(context!!, R.string.rate_pin_max_message, Toast.LENGTH_SHORT).show()
    }

    override fun showEmptyRatesView(show: Boolean, isAnime: Boolean?) {
        val b = _binding ?: return
        if (isAnime != null) {
            b.rateEmptyView.actionBtn.setText(if (isAnime) R.string.rate_empty_anime else R.string.rate_empty_manga)
            b.rateEmptyView.actionBtn.onClick {
                getPresenter().onEmptyRateClicked(isAnime)
            }
        }

        b.rateEmptyView.root.visibility = if (show) View.VISIBLE else View.GONE
        if (show) _fragmentPlaceholdersBinding?.emptyContentView?.gone()
    }

    override fun showNeedAuthView(show: Boolean) {
        val b = _binding ?: return
        b.authLayout.root.visibility = if (show) View.VISIBLE else View.GONE
        if (show) {
            b.includedLayoutDefaultList.refreshLayout.gone()
            b.rateEmptyView.root.gone()
            _fragmentPlaceholdersBinding?.emptyContentView?.gone()
            _fragmentPlaceholdersBinding?.networkErrorView?.gone()
        }
    }

    override fun showRateMessage(taskId: Int, message: String, rateId: Long) {
        val b = _binding ?: return
        Snackbar.make(b.coordinator, message, Snackbar.LENGTH_LONG)
                .floatingStyle(context!!)
                .setActionTextColor(context!!.colorAttr(R.attr.colorSecondary))
                .setAction(R.string.common_cancel_variant) { getPresenter().onTaskCanceled(taskId, rateId) }
                .show()
    }

    override fun showNetworkView() {
        _fragmentPlaceholdersBinding?.networkErrorView?.visible()
        _fragmentPlaceholdersBinding?.emptyContentView?.gone()
    }

    override fun hideNetworkView() {
        _fragmentPlaceholdersBinding?.networkErrorView?.gone()
        _fragmentPlaceholdersBinding?.emptyContentView?.gone()
    }
}