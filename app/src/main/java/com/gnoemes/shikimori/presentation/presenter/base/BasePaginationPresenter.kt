package com.gnoemes.shikimori.presentation.presenter.base

import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.NetworkException
import com.gnoemes.shikimori.entity.app.domain.exceptions.ServiceCodeException
import com.gnoemes.shikimori.presentation.presenter.common.paginator.PageOffsetPaginator
import com.gnoemes.shikimori.presentation.presenter.common.paginator.ViewController
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationView
import io.reactivex.Single

//TODO rename presenter or add more abstractions to paginator based on type (pageOffset, dateOffset etc.)
abstract class BasePaginationPresenter<Items : Any, View : BasePaginationView> : BaseNetworkPresenter<View>(), ViewController<Items> {

    private var paginator: PageOffsetPaginator<Items>? = null

    protected abstract fun getPaginatorRequestFactory(): (Int) -> Single<List<Items>>

    override fun initData() {
        super.initData()

        loadData()
    }

    protected open fun loadData() {
        if (paginator == null) {
            paginator = PageOffsetPaginator(getPaginatorRequestFactory(), this)
        }

        paginator?.refresh()
    }

    protected open fun destroyPaginator() {
        paginator?.release()
        paginator = null
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyPaginator()
    }

    open fun loadNextPage() {
        paginator?.loadNewPage()
    }

    open fun onRefresh() {
        loadData()
    }

    override fun showData(show: Boolean, data: List<Items>) {
        if (show) {
            viewState.showData(data)
            viewState.hideNetworkView()
        }
        viewState.showContent(show)
    }

    override fun showEmptyView(show: Boolean) {
        viewState.showContent(!show)
        if (show) viewState.showEmptyView()
        else viewState.hideEmptyView()
    }

    override fun showRefreshProgress(show: Boolean) {
        if (show) viewState.onShowLoading()
        else viewState.onHideLoading()
    }

    override fun showEmptyProgress(show: Boolean) {
        if (show) viewState.onShowLoading()
        else viewState.onHideLoading()
    }

    override fun showPageProgress(show: Boolean) {
        if (show) viewState.showPageLoading()
        else viewState.hidePageLoading()
    }

    override fun showError(throwable: Throwable) {
        processErrors(throwable)
    }

    override fun showEmptyError(show: Boolean, throwable: Throwable?) {
        if ((throwable as? BaseException)?.tag != NetworkException.TAG
                && (throwable as? BaseException)?.tag != ServiceCodeException.TAG) {
            if (show) viewState.showEmptyView()
        }

        if (!show) {
            viewState.hideNetworkView()
            viewState.hideEmptyView()
        }

        throwable?.let { processErrors(it) }
    }

    override fun onAllData() {
        viewState.onAllData()
    }

}