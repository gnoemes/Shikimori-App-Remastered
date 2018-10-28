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
import kotlinx.android.synthetic.main.fragment_rates_container.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router

class RatesContainerFragment : BaseFragment<RatesContainerPresenter, RatesContainerView>(), RatesContainerView, RouterProvider {

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

    private lateinit var spinner: ReSpinner
    private lateinit var adapter: RatePagerAdapter

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
        toolbar?.apply {
            spinner = ReSpinner(context)
            spinner.adapter = ArrayAdapter<String>(context, R.layout.item_spinner_toolbar, context.resources.getStringArray(R.array.rate_types))
            spinner.background = spinner.background.apply { tint(context.colorAttr(R.attr.colorOnSurface)) }
            spinner.itemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> getPresenter().onChangeType(Type.ANIME)
                    1 -> getPresenter().onChangeType(Type.MANGA)
                }
            }
            title = null
            addView(spinner)

        }
        adapter = RatePagerAdapter(childFragmentManager)
        savedInstanceState.ifNotNull {
            spinner.setSelection(it.getInt(SPINNER_KEY, 0), false)
        }
        emptyContentView.setText(R.string.rate_empty)
        ratesContainer.adapter = this@RatesContainerFragment.adapter
        ratesContainer.offscreenPageLimit = 6
        tabLayout.setupWithViewPager(ratesContainer)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SPINNER_KEY, spinner.selectedItemPosition)
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

    override fun setData(type: Type, it: List<Pair<RateStatus, String>>) {
        adapter.setItems(type, it)
        ratesContainer.setCurrentItem(0, true)
    }

    override fun onShowLoading() {
        super<BaseFragment>.onShowLoading()
        rateAppBarLayout.gone()
        ratesContainer.gone()
    }

    override fun onHideLoading() {
        super<BaseFragment>.onHideLoading()
        rateAppBarLayout.visible()
        ratesContainer.visible()
    }

    override fun showNetworkView(block: Boolean) {
        super<BaseFragment>.showNetworkView(block)
        rateAppBarLayout.gone()
        ratesContainer.gone()
    }

    override fun hideNetworkView() {
        super<BaseFragment>.hideNetworkView()
        rateAppBarLayout.visible()
        ratesContainer.visible()
    }

    inner class RatePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private var type: Type = Type.ANIME
        private var items = mutableListOf<Pair<RateStatus, String>>()

        fun setItems(newType: Type, newItems: List<Pair<RateStatus, String>>) {
            type = newType
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }

        override fun getItem(position: Int): Fragment {
            return Fragment()
        }

        override fun getCount(): Int = items.size

        override fun getPageTitle(position: Int): CharSequence? {
            return items[position].second
        }
    }

}