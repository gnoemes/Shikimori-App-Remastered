package com.gnoemes.shikimori.presentation.view.rates

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.presentation.RateCategory
import com.gnoemes.shikimori.entity.rates.presentation.RateNavigationData
import com.gnoemes.shikimori.presentation.presenter.rates.RatesContainerPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.base.fragment.TabRootFragment
import com.gnoemes.shikimori.utils.*
import com.google.android.material.internal.NavigationMenuView
import com.santalu.widget.ReSpinner
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_rates_container.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class RatesContainerFragment : BaseFragment<RatesContainerPresenter, RatesContainerView>(), RatesContainerView, RouterProvider, HasSupportFragmentInjector, TabRootFragment {

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @InjectPresenter
    lateinit var containerPresenter: RatesContainerPresenter

    @ProvidePresenter
    fun providePresenter(): RatesContainerPresenter {
        return presenterProvider.get().apply {
            localRouter = (parentFragment as RouterProvider).localRouter
            val data = arguments?.getParcelable<RateNavigationData>(AppExtras.ARGUMENT_RATE_DATA)
            data?.let {
                userId = it.userId
                type = it.type
                priorityStatus = it.status
            }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector

    private var spinner: ReSpinner? = null

    companion object {
        fun newInstance(data: RateNavigationData?) = RatesContainerFragment().withArgs {
            putParcelable(AppExtras.ARGUMENT_RATE_DATA, data)
        }

        private const val SPINNER_KEY = "SPINNER_KEY"
        private const val DRAWER_KEY = "DRAWER_KEY"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getFragmentLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        spinner = ReSpinner(context!!)
                .apply {
                    adapter = ArrayAdapter<String>(context, R.layout.item_spinner_toolbar, context.resources.getStringArray(R.array.rate_types))
                    background = background.apply { tint(context.colorAttr(R.attr.colorOnSurface)); mutate() }
                    itemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        when (position) {
                            0 -> getPresenter().onChangeType(Type.ANIME)
                            1 -> getPresenter().onChangeType(Type.MANGA)
                        }
                    }
                }

        toolbar?.apply {
            title = null
            addView(spinner)
            setNavigationOnClickListener { toggleDrawer() }
            inflateMenu(R.menu.menu_rates)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_random -> getPresenter().onRandomClicked()
                }
                true
            }
        }

        drawer.apply {
            val toggle = ActionBarDrawerToggle(
                    activity, drawer, toolbar, R.string.rate_drawer_open, R.string.rate_drawer_close)

            addDrawerListener(toggle)
            setViewScale(Gravity.START, 0.9f)
            setRadius(Gravity.START, 35f)
            setViewElevation(Gravity.START, 20f)
            toggle.syncState()
        }

        savedInstanceState.ifNotNull {
            spinner?.setSelection(it.getInt(SPINNER_KEY, 0), false)
            drawer.post {
                if (it.getBoolean(DRAWER_KEY)) openDrawer()
                else closeDrawer()
            }
        }

        emptyContentView?.setText(R.string.rate_empty)
        networkErrorView.apply {
            setText(R.string.common_error_message_without_pull)
            callback = { getPresenter().initData() }
            showButton()
        }
        progressBar?.gone()

        navView.setItemBackgroundResource(getRateBackground(RateStatus.WATCHING.ordinal))
        navView.itemTextColor = ContextCompat.getColorStateList(context!!, getRateTextColor(RateStatus.WATCHING.ordinal))
        navView.setNavigationItemSelectedListener {
            getPresenter().onChangeStatus(RateStatus.values()[it.itemId])
            true
        }

        val menuContainer = navView.findViewById<NavigationMenuView>(R.id.design_navigation_view)
        menuContainer.layoutParams = FrameLayout.LayoutParams(menuContainer.layoutParams).apply { height = ViewGroup.LayoutParams.WRAP_CONTENT; gravity = Gravity.CENTER_VERTICAL }
    }

    private fun toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            openDrawer()
        }
    }

    private fun openDrawer() {
        drawer.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer() {
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SPINNER_KEY, spinner?.selectedItemPosition ?: 0)
        outState.putBoolean(DRAWER_KEY, drawer?.isDrawerOpen(GravityCompat.START) ?: false)
    }

    override fun onTabRootAction() {
        getPresenter().onRandomClicked()
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
        view.setTextColor(ContextCompat.getColorStateList(context!!, getRateTextColor(ordinal)))
        view.isSelected = isSelected
        return view
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): RatesContainerPresenter = containerPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_rates_container

    override val localRouter: Router
        get() = (parentFragment as RouterProvider).localRouter
    override val localNavigator: Navigator
        get() = (parentFragment as RouterProvider).localNavigator

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) closeDrawer()
        else super.onBackPressed()
    }

    override fun showStatusFragment(id: Long, type: Type, status: RateStatus) {
        closeDrawer()
        val oldFragment = childFragmentManager.findFragmentByTag(type.name.plus(status.status))
        if (oldFragment == null) {
            val newFragment = RateFragment.newInstance(id, type, status)
            childFragmentManager.beginTransaction()
                    .replace(R.id.rateContainer, newFragment, type.name.plus(status.status))
                    .commitNow()
        }
    }

    override fun setNavigationItems(items: List<RateCategory>) {
        navView.menu.apply {
            val checkedId = navView.checkedItem?.itemId ?: 0
            clear()
            items.forEach {
                add(0, it.status.ordinal, it.status.ordinal, it.localizedCategory)
                findItem(it.status.ordinal)?.actionView = getActionTextView(it.status.ordinal, it.count, checkedId == it.status.ordinal)
            }
            if (items.isEmpty()) {
                add(R.string.rate_empty)
            }

            setGroupCheckable(0, true, true)
            navView.setCheckedItem(checkedId)
        }

    }

    override fun selectRateStatus(rateStatus: RateStatus) {
        navView.apply {
            setItemBackgroundResource(getRateBackground(rateStatus.ordinal))
            itemTextColor = ContextCompat.getColorStateList(context!!, getRateTextColor(rateStatus.ordinal))
            setCheckedItem(rateStatus.ordinal)
            (menu.findItem(rateStatus.ordinal)?.actionView as? TextView)?.isSelected = true
        }
    }

    override fun showContainer() {
        rateContainer.visible()
    }

    override fun hideContainer() {
        rateContainer.gone()
    }

    override fun selectType(type: Type) {
        if (type == Type.ANIME || type == Type.MANGA) {
            spinner?.setSelection(type.ordinal, false)
        }
    }

    override fun showRandomRate(type: Type, status: RateStatus) {
        val fragment = childFragmentManager.findFragmentByTag(type.name.plus(status.status))
        (fragment as? RandomRateListener)?.showRandomRate()
    }
}