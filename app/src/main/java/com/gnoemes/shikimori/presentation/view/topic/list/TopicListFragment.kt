package com.gnoemes.shikimori.presentation.view.topic.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.presenter.topic.list.TopicListPresenter
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProvider
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.shikimorimain.ShikimoriMainFragment
import com.gnoemes.shikimori.presentation.view.topic.list.adapter.TopicListAdapter
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class TopicListFragment : BasePaginationFragment<TopicViewModel, TopicListPresenter, TopicListView>(), TopicListView {

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

    private val topicAdapter by lazy { TopicListAdapter(imageLoader, resourceProvider, converter, getPresenter()::onContentClicked) }

    override val adapter: BasePaginationAdapter
        get() = topicAdapter

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

        emptyContentView.setText(R.string.search_nothing)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): TopicListPresenter = topicPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_topic_list

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setMyClubsEmptyText() {
        emptyContentView.setText(R.string.forum_my_clubs_empty)
    }
}