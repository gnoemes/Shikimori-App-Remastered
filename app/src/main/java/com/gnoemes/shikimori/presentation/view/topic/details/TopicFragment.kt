package com.gnoemes.shikimori.presentation.view.topic.details

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.comment.presentation.CommentViewModel
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.topic.presentation.TopicContentViewModel
import com.gnoemes.shikimori.entity.topic.presentation.TopicUserViewModel
import com.gnoemes.shikimori.presentation.presenter.topic.details.TopicPresenter
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.topic.details.adapter.CommentsAdapter
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicContentViewHolder
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicUserViewHolder
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.images.ImageLoader
import kotlinx.android.synthetic.main.fragment_topic.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_topic_comments.*
import kotlinx.android.synthetic.main.layout_topic_comments.view.*
import kotlinx.android.synthetic.main.layout_topic_linked.*
import javax.inject.Inject

class TopicFragment : BasePaginationFragment<CommentViewModel, TopicPresenter, TopicView>(), TopicView {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var converter: DateTimeConverter

    @InjectPresenter
    lateinit var topicPresenter: TopicPresenter

    @ProvidePresenter
    fun providePresenter(): TopicPresenter {
        return presenterProvider.get().apply {
            localRouter = (parentFragment as RouterProvider).localRouter
            id = arguments?.getLong(AppExtras.ARGUMENT_TOPIC_ID) ?: Constants.NO_ID
        }
    }

    companion object {
        fun newInstance(id: Long) = TopicFragment().withArgs { putLong(AppExtras.ARGUMENT_TOPIC_ID, id) }
        private const val PREVIOUS_KEY = "PREVIOUS_KEY"
    }

    private val commentsAdapter by lazy { CommentsAdapter(imageLoader, getPresenter()::onContentClicked).apply { if (!hasObservers()) setHasStableIds(true) } }

    private lateinit var userHolder: TopicUserViewHolder
    private lateinit var contentHolder: TopicContentViewHolder

    private var isPrevious: Boolean = false

    override val adapter: BasePaginationAdapter
        get() = commentsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            isPrevious = it.getBoolean(PREVIOUS_KEY, false)
        }

        ViewCompat.setNestedScrollingEnabled(scrollView, false)
        userHolder = TopicUserViewHolder(userLayout, imageLoader, getPresenter()::onContentClicked)
        contentHolder = TopicContentViewHolder(topicLayout, getPresenter()::onContentClicked)

        toolbar.addBackButton { getPresenter().onBackPressed() }
        toolbar.title = null

        with(recyclerView) {
            adapter = this@TopicFragment.adapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            itemAnimator = null
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        commentsMore.setOnClickListener { isPrevious = false; getPresenter().loadNextPage() }
        commentsBefore.setOnClickListener { isPrevious = true; getPresenter().onPreviousClicked() }

        networkErrorView.apply {
            setText(R.string.common_error_message_without_pull)
            callback = { getPresenter().initData() }
            showButton()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(PREVIOUS_KEY, isPrevious)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): TopicPresenter = topicPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_topic

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(data: List<Any>) {
        commentsAdapter.bindItems(data, isPrevious)
    }

    override fun setUserData(item: TopicUserViewModel) {
        userHolder.bind(item)
    }

    override fun setContentData(item: TopicContentViewModel) {
        contentHolder.bind(item)
    }

    override fun setCommentsText(text: String?) {
        commentsMore.text = text
    }

    //TODO holder and implementation for manga
    override fun setLinkedContent(linked: LinkedContent?) {
        context?.let { context ->
            linkedLayout.visibleIf { linked != null }
            if (linked != null) {
                linkedLayout.setOnClickListener { getPresenter().onContentClicked(linked.linkedType, linked.linkedId) }
                imageLoader.setImageWithPlaceHolder(imageView, linked.imageUrl)
                linkedTitleView.text = linked.linkedName

                if (linked is Anime) {
                    fun convertStatus(status: Status): String {
                        return when (status) {
                            Status.ANONS -> context.getString(R.string.status_anons)
                            Status.ONGOING -> context.getString(R.string.status_ongoing)
                            Status.RELEASED -> context.getString(R.string.status_released)
                            else -> context.getString(R.string.error_no_data)
                        }
                    }

                    fun convertType(type: AnimeType, episodes: Int): String {
                        return String.format(context.getString(R.string.type_pattern_without_duration), type.type.toUpperCase(),
                                episodes.unknownIfZero())
                    }

                    val type = convertType(linked.type, linked.episodes)
                    val season = converter.convertAnimeSeasonToString(linked.dateAired)
                    val status = convertStatus(linked.status)

                    val typeText = context.getString(R.string.details_type).toBold().append(" ").append(type)
                    val seasonText = context.getString(R.string.details_season).toBold().append(" ").append(season)
                    val statusText = context.getString(R.string.details_status).toBold().append(" ").append(status)

                    typeView.text = typeText
                    seasonView.text = seasonText
                    statusView.text = statusText
                }
            }
        }
    }

    override fun showCommentsLoading(show: Boolean) {
        commentsLayout.commentProgress.visibleIf { show }
        recyclerView.visibleIf { !show }
    }

    override fun onShowLoading() {
        progressBar.visible()
    }

    override fun onHideLoading() {
        progressBar.gone()
    }

    override fun setCommentsCount(count: Long) {
        context?.let {
            val text = it.getString(R.string.common_comments) + " ($count):"
            commentTitleView.text = text
        }
    }

    override fun showEmptyView() {
        commentsAdapter.showEmptyView()
    }

    override fun hideEmptyView() {
        commentsAdapter.hideEmptyView()
    }

    override fun showContent(show: Boolean) {
        scrollView.visibleIf { show }
    }

    override fun showCommentsMore(show: Boolean) {
        commentsMore.visibleIf { show }
    }

    override fun showPreviousComments(show: Boolean) {
        commentsBefore.visibleIf { show }
    }

}