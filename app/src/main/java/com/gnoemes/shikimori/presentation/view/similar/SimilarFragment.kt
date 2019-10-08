package com.gnoemes.shikimori.presentation.view.similar

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.CommonNavigationData
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.presenter.similar.SimilarPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.rates.status.RateStatusDialog
import com.gnoemes.shikimori.presentation.view.similar.adapter.SimilarAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class SimilarFragment : BaseFragment<SimilarPresenter, SimilarView>(), SimilarView, RateStatusDialog.RateStatusCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var similarPresenter: SimilarPresenter

    @ProvidePresenter
    fun provide() = presenterProvider.get().apply {
        localRouter = (parentFragment as RouterProvider).localRouter
        navigationData = arguments?.getParcelable(DATA_KEY)!!
    }

    companion object {
        fun newInstance(data: CommonNavigationData) = SimilarFragment().withArgs { putParcelable(DATA_KEY, data) }
        private const val DATA_KEY = "DATA_KEY"
    }

    private val adapter by lazy { SimilarAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onShowStatusDialog) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            addBackButton { getPresenter().onBackPressed() }
            setTitle(R.string.common_similar)
        }

        with(recyclerView) {
            adapter = this@SimilarFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(8)))
        }

        refreshLayout.background = ColorDrawable(context!!.colorAttr(R.attr.colorSurface))
        refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }

        emptyContentView.setText(R.string.similar_empty_description)
    }

    override fun onStatusChanged(id: Long, newStatus: RateStatus) {
        getPresenter().onChangeRateStatus(id, newStatus)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): SimilarPresenter = similarPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_default_list

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(items: List<Any>) {
        adapter.bindItems(items)
    }

    override fun showStatusDialog(id: Long, title: String, status: RateStatus?, anime: Boolean) {
        hideSoftInput()
        val dialog = RateStatusDialog.newInstance(id, title, status, anime)
        dialog.show(childFragmentManager, "StatusDialog")
    }

    override fun showContent(show: Boolean) = recyclerView.visibleIf { show }
    override fun onShowLoading() = refreshLayout.showRefresh()
    override fun onHideLoading() = refreshLayout.hideRefresh()

}