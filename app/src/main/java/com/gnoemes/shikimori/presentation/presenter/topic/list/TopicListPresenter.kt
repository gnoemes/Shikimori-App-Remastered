package com.gnoemes.shikimori.presentation.presenter.topic.list

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.topic.TopicInteractor
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.paginator.PageOffsetPaginator
import com.gnoemes.shikimori.presentation.presenter.common.paginator.ViewController
import com.gnoemes.shikimori.presentation.presenter.topic.converter.TopicViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProvider
import com.gnoemes.shikimori.presentation.view.topic.list.TopicListView
import io.reactivex.Single
import javax.inject.Inject

//TODO base pagination presenter
@InjectViewState
class TopicListPresenter @Inject constructor(
        private val interactor: TopicInteractor,
        private val converter: TopicViewModelConverter,
        private val topicResourceProvider: TopicResourceProvider
) : BaseNetworkPresenter<TopicListView>() {

    private var paginator: PageOffsetPaginator<TopicViewModel>? = null
    lateinit var type: ForumType

    override fun initData() {
        super.initData()

        viewState.setTitle(topicResourceProvider.getTopicName(type))
        loadData()
    }

    private fun loadData() {
        if (paginator == null) {
            paginator = PageOffsetPaginator(getPaginatorRequestFactory(), viewController)
        }

        paginator?.refresh()
    }

    fun loadNextPage() {
        paginator?.loadNewPage()
    }

    fun onRefresh() {
        loadData()
    }

    private fun getPaginatorRequestFactory(): (Int) -> Single<List<TopicViewModel>> {
        return { page: Int -> interactor.getList(type, page).map(converter) }
    }

    private fun destroyPaginator() {
        paginator?.release()
        paginator = null
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyPaginator()
    }

    private val viewController = object : ViewController<TopicViewModel> {
        override fun showData(show: Boolean, data: List<TopicViewModel>) {
            if (show) viewState.showData(data)
            else viewState.hideData()
        }

        override fun showEmptyView(show: Boolean) {
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
            if (show) viewState.showEmptyView()
            else viewState.hideEmptyView()

            throwable?.let { processErrors(it) }
        }
    }

}