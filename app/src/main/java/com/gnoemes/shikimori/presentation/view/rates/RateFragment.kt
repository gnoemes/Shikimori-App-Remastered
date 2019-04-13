package com.gnoemes.shikimori.presentation.view.rates

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.presenter.rates.RatePresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.fragment.EditRateFragment
import com.gnoemes.shikimori.presentation.view.rates.adapter.RateAdapter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.visible
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class RateFragment : BaseFragment<RatePresenter, RateView>(), RateView, EditRateFragment.RateDialogCallback, RandomRateListener {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var settings: SettingsSource

    @Inject
    lateinit var resourceProvider: RatingResourceProvider

    @InjectPresenter
    lateinit var ratePresenter: RatePresenter

    @ProvidePresenter
    fun providePresenter(): RatePresenter {
        ratePresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            ratePresenter.localRouter = (parentFragment as RouterProvider).localRouter
        }

        arguments.ifNotNull {
            ratePresenter.userId = it.getLong(AppExtras.ARGUMENT_USER_ID)
            ratePresenter.type = it.getSerializable(AppExtras.ARGUMENT_TYPE) as Type
            ratePresenter.rateStatus = it.getSerializable(AppExtras.ARGUMENT_RATE_STATUS) as RateStatus
        }

        return ratePresenter
    }

    private val adapter by lazy { RateAdapter(settings, imageLoader, resourceProvider, getPresenter()::onContentClicked, { getPresenter().onAction(it) }, { sort, des -> getPresenter().onSortChanged(sort, des) }, { getPresenter().loadNewPage() }) }

    companion object {
        fun newInstance(userId: Long, type: Type, status: RateStatus) = RateFragment().withArgs {
            putLong(AppExtras.ARGUMENT_USER_ID, userId)
            putSerializable(AppExtras.ARGUMENT_TYPE, type)
            putSerializable(AppExtras.ARGUMENT_RATE_STATUS, status)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if (recyclerView.layoutManager == null)
            with(recyclerView) {
                adapter = this@RateFragment.adapter
                layoutManager = LinearLayoutManager(context).apply { initialPrefetchItemCount = 5 }
                itemAnimator = DefaultItemAnimator()
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                setHasFixedSize(true)
            }

        emptyContentView.setText(R.string.rate_empty)
        refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }
        progressBar?.gone()
        toolbar?.gone()
    }

    override fun onUpdateRate(rate: UserRate) {
        getPresenter().onUpdateRate(rate)
    }

    override fun onDeleteRate(id: Long) {
        getPresenter().onDeleteRate(id)
    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    override fun showRandomRate() {
        getPresenter().onOpenRandom()
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
        val parcelable = recyclerView.layoutManager?.onSaveInstanceState()
        adapter.setData(data)
        recyclerView.visible()
        recyclerView.layoutManager?.onRestoreInstanceState(parcelable)
    }

    override fun showRateDialog(userRate: UserRate) {
        val dialog = EditRateFragment.newInstance(rate = userRate, isAnime = userRate.targetType == Type.ANIME)
        dialog.show(childFragmentManager, "RateTag")
    }

    override fun hideList() {
        recyclerView.gone()
    }

    override fun showPageProgress(show: Boolean) {
        postViewAction { adapter.showProgress(show) }
    }

    override fun showRefresh() {
        refreshLayout.isRefreshing = true
    }

    override fun hideRefresh() {
        refreshLayout.isRefreshing = false
    }

    override fun showNetworkView() {}

    override fun hideNetworkView() {}
}