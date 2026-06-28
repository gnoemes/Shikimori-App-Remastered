package com.gnoemes.shikimori.presentation.view.similar

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentDefaultListBinding
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
import javax.inject.Inject

class SimilarFragment : BaseFragment<SimilarPresenter, SimilarView>(), SimilarView, RateStatusDialog.RateStatusCallback {

    private var _viewBinding: FragmentDefaultListBinding? = null
    private val viewBinding: FragmentDefaultListBinding? get() = _viewBinding

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var similarPresenter: SimilarPresenter

    @ProvidePresenter
    fun provide() = presenterProvider.get().apply {
        localRouter = (parentFragment as RouterProvider).localRouter
        @Suppress("DEPRECATION")
        navigationData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(DATA_KEY, CommonNavigationData::class.java)!!
        } else {
            arguments?.getParcelable(DATA_KEY)!!
        }
    }

    companion object {
        fun newInstance(data: CommonNavigationData) = SimilarFragment().withArgs { putParcelable(DATA_KEY, data) }
        private const val DATA_KEY = "DATA_KEY"
    }

    private val adapter by lazy { SimilarAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onShowStatusDialog) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentDefaultListBinding.bind(view.findViewById(R.id.fragment_content))
        val vb = _viewBinding ?: return

        toolbarBinding?.toolbar?.apply {
            addBackButton { getPresenter().onBackPressed() }
            setTitle(R.string.common_similar)
        }

        with(vb.includedLayoutDefaultList.recyclerView) {
            adapter = this@SimilarFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(8)))
        }

        vb.includedLayoutDefaultList.refreshLayout.background = ColorDrawable(context!!.colorAttr(R.attr.colorSurface))
        vb.includedLayoutDefaultList.refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }

        placeholdersBinding?.emptyContentView?.setText(R.string.similar_empty_description)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
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

    override fun showContent(show: Boolean) { viewBinding?.includedLayoutDefaultList?.recyclerView?.visibleIf { show } }
    override fun onShowLoading() { viewBinding?.includedLayoutDefaultList?.refreshLayout?.showRefresh() }
    override fun onHideLoading() { viewBinding?.includedLayoutDefaultList?.refreshLayout?.hideRefresh() }

}