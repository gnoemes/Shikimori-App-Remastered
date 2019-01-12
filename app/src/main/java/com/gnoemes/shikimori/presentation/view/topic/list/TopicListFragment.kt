package com.gnoemes.shikimori.presentation.view.topic.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.presentation.presenter.topic.list.TopicListPresenter
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProvider
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.shikimorimain.ShikimoriMainFragment
import com.gnoemes.shikimori.presentation.view.topic.list.adapter.TopicListAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.images.ImageLoader
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class TopicListFragment : BaseFragment<TopicListPresenter, TopicListView>(), TopicListView {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var resourceProvider: TopicResourceProvider

    @Inject
    lateinit var converter: DateTimeConverter

    @InjectPresenter
    lateinit var topicPresenter: TopicListPresenter

    @ProvidePresenter
    fun providePresenter(): TopicListPresenter {
        topicPresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            topicPresenter.localRouter = (it as RouterProvider).localRouter
        }

        arguments?.let {
            topicPresenter.type = it.getSerializable(AppExtras.ARGUMENT_FORUM_TYPE) as ForumType
        }

        return topicPresenter
    }

    companion object {
        fun newInstance(type: ForumType) = TopicListFragment().withArgs { putSerializable(AppExtras.ARGUMENT_FORUM_TYPE, type) }
    }

    private val adapter by lazy { TopicListAdapter(imageLoader, resourceProvider, converter, getPresenter()::onContentClicked) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (parentFragment is ShikimoriMainFragment) toolbar?.gone()
        else toolbar?.addBackButton { topicPresenter.onBackPressed() }

        with(recyclerView) {
            adapter = this@TopicListFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(nextPageListener)
        }

        refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }
    }

    //TODO make global pagination listener
    private val nextPageListener = object : RecyclerView.OnScrollListener() {
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

    override fun onDestroyView() {
        recyclerView.removeOnScrollListener(nextPageListener)
        super.onDestroyView()
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): TopicListPresenter = topicPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_topic_list

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(data: List<Any>) {
        adapter.bindItems(data)
        recyclerView.visible()
    }

    override fun hideData() {
        recyclerView.gone()
    }

    override fun onShowLoading() = refreshLayout.showRefresh()
    override fun onHideLoading() = refreshLayout.hideRefresh()
    override fun showPageLoading() = postViewAction { adapter.showProgress(true) }
    override fun hidePageLoading() = postViewAction { adapter.showProgress(false) }
}