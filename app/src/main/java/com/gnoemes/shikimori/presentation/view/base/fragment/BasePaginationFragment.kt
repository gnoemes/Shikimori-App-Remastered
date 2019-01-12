package com.gnoemes.shikimori.presentation.view.base.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.presentation.presenter.base.BasePaginationPresenter
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.hideRefresh
import com.gnoemes.shikimori.utils.showRefresh
import com.gnoemes.shikimori.utils.visible
import kotlinx.android.synthetic.main.layout_default_list.*

abstract class BasePaginationFragment<Items : Any, Presenter : BasePaginationPresenter<Items, View>, View : BasePaginationView> : BaseFragment<Presenter, View>(), BasePaginationView {

    abstract val adapter: BasePaginationAdapter

    protected open val nextPageListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val manager = (recyclerView.layoutManager as LinearLayoutManager)
            val visibleItemPosition = manager.findLastCompletelyVisibleItemPosition() + Constants.DEFAULT_LIMIT / 2
            val itemCount = manager.itemCount

            if (!adapter.isProgress() && visibleItemPosition >= itemCount) {
                getPresenter().loadNextPage()
            }
        }
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout?.setOnRefreshListener { getPresenter().onRefresh() }
    }

    override fun onDestroyView() {
        recyclerView?.removeOnScrollListener(nextPageListener)
        super.onDestroyView()
    }

    override fun showData(data: List<Any>) {
        adapter.bindItems(data)
        recyclerView?.visible()
    }

    override fun hideData() {
        recyclerView?.gone()
    }

    override fun onShowLoading() = refreshLayout.showRefresh()
    override fun onHideLoading() = refreshLayout.hideRefresh()
    override fun showPageLoading() = postViewAction { adapter.showProgress(true) }
    override fun hidePageLoading() = postViewAction { adapter.showProgress(false) }
}