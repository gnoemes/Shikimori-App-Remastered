package com.gnoemes.shikimori.presentation.view.rates

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.presenter.rates.RatesContainerPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.utils.*
import com.santalu.widget.ReSpinner
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_rates_container.*
import kotlinx.android.synthetic.main.layout_appbar_tabs.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class RatesContainerFragment : BaseFragment<RatesContainerPresenter, RatesContainerView>(), RatesContainerView, RouterProvider, HasSupportFragmentInjector {

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @InjectPresenter
    lateinit var containerPresenter: RatesContainerPresenter

    @ProvidePresenter
    fun providePresenter(): RatesContainerPresenter {
        containerPresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            containerPresenter.localRouter = (parentFragment as RouterProvider).localRouter
        }

        arguments.ifNotNull {
            containerPresenter.userId = it.getLong(AppExtras.ARGUMENT_USER_ID)
        }

        return containerPresenter
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector

    private var spinner: ReSpinner? = null

    companion object {
        fun newInstance(id: Long?) = RatesContainerFragment().withArgs {
            putLong(AppExtras.ARGUMENT_USER_ID, id ?: Constants.NO_ID)
        }

        private const val SPINNER_KEY = "SPINNER_KEY"
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
        }
        savedInstanceState.ifNotNull {
            spinner?.setSelection(it.getInt(SPINNER_KEY, 0), false)
        }

        tabLayout.setupWithViewPager(ratesContainer)
        emptyContentView.setText(R.string.rate_empty)
        progressBar?.gone()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SPINNER_KEY, spinner?.selectedItemPosition ?: 0)
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

    override fun setData(id: Long, type: Type, it: List<Pair<RateStatus, String>>) {
        ratesContainer.adapter = null
        ratesContainer.adapter = RatePagerAdapter(childFragmentManager, id, type, it)
        ratesContainer.offscreenPageLimit = it.size
    }

    override fun onShowLoading() {
        super<BaseFragment>.onShowLoading()
        coordinator.gone()
    }

    override fun onHideLoading() {
        super<BaseFragment>.onHideLoading()
        coordinator.visible()
    }

    override fun showNetworkView(block: Boolean) {
        super<BaseFragment>.showNetworkView(block)
        coordinator.gone()
    }

    override fun hideNetworkView() {
        super<BaseFragment>.hideNetworkView()
        coordinator.visible()
    }

    inner class RatePagerAdapter(fm: FragmentManager,
                                 val userId: Long,
                                 val type: Type,
                                 val items: List<Pair<RateStatus, String>>
    ) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return RateFragment.newInstance(userId, type, items[position].first)
        }

        override fun getCount(): Int = items.size

        override fun getPageTitle(position: Int): CharSequence? {
            return items[position].second
        }
    }

}