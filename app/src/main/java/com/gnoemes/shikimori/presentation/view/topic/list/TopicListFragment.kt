package com.gnoemes.shikimori.presentation.view.topic.list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.LayoutDefaultListBinding
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.presenter.topic.list.TopicListPresenter
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
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
import javax.inject.Inject

class TopicListFragment : BasePaginationFragment<TopicViewModel, TopicListPresenter, TopicListView>(), TopicListView {

    @Inject
    lateinit var imageLoader: ImageLoader

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
            @Suppress("DEPRECATION")
            topicPresenter.type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(AppExtras.ARGUMENT_FORUM_TYPE, ForumType::class.java) as ForumType
            } else {
                it.getSerializable(AppExtras.ARGUMENT_FORUM_TYPE) as ForumType
            }
        }

        return topicPresenter
    }

    companion object {
        fun newInstance(type: ForumType) = TopicListFragment().withArgs { putSerializable(AppExtras.ARGUMENT_FORUM_TYPE, type) }
    }

    private val topicAdapter by lazy { TopicListAdapter(imageLoader, converter, getPresenter()::onContentClicked) }

    override val adapter: BasePaginationAdapter
        get() = topicAdapter

    private lateinit var topicListBinding: LayoutDefaultListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        topicListBinding = LayoutDefaultListBinding.bind(view.findViewById(R.id.included_layout_default_list))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (parentFragment is ShikimoriMainFragment) toolbarBinding?.toolbar?.gone()
        else toolbarBinding?.toolbar?.addBackButton { topicPresenter.onBackPressed() }

        with(topicListBinding.recyclerView) {
            adapter = this@TopicListFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(nextPageListener)
        }
        ViewCompat.setNestedScrollingEnabled(topicListBinding.recyclerView, false)

        placeholdersBinding?.networkErrorView?.setText(R.string.common_error_message)
        placeholdersBinding?.emptyContentView?.setText(R.string.search_nothing)
    }

    override fun onDestroyView() {
        topicListBinding.recyclerView.removeOnScrollListener(nextPageListener)
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

    override fun setMyClubsEmptyText() {
        placeholdersBinding?.emptyContentView?.setText(R.string.forum_my_clubs_empty)
    }
}