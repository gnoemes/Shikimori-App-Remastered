package com.gnoemes.shikimori.presentation.view.topic.details

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentTopicBinding
import com.gnoemes.shikimori.databinding.LayoutDefaultPlaceholdersBinding
import com.gnoemes.shikimori.databinding.LayoutProgressBinding
import com.gnoemes.shikimori.databinding.LayoutToolbarBinding
import com.gnoemes.shikimori.databinding.LayoutTopicBinding
import com.gnoemes.shikimori.databinding.LayoutTopicCommentsBinding
import com.gnoemes.shikimori.databinding.LayoutTopicLinkedBinding
import com.gnoemes.shikimori.databinding.LayoutTopicUserBinding
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.comment.presentation.CommentViewModel
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaType
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

    private var _fragmentBinding: FragmentTopicBinding? = null
    private val fragmentBinding: FragmentTopicBinding? get() = _fragmentBinding

    override val adapter: BasePaginationAdapter
        get() = commentsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            isPrevious = it.getBoolean(PREVIOUS_KEY, false)
        }

        _fragmentBinding = FragmentTopicBinding.bind(view)

        fragmentBinding?.let { b ->
            ViewCompat.setNestedScrollingEnabled(b.scrollView, false)
            userHolder = TopicUserViewHolder(b.userLayout, imageLoader, getPresenter()::onContentClicked)
            contentHolder = TopicContentViewHolder(b.topicLayout, getPresenter()::onContentClicked)

            with(b.commentsLayout.recyclerView) {
                adapter = this@TopicFragment.adapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
                itemAnimator = null
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }

            b.commentsLayout.commentsMore.setOnClickListener { isPrevious = false; getPresenter().loadNextPage() }
            b.commentsLayout.commentsBefore.setOnClickListener { isPrevious = true; getPresenter().onPreviousClicked() }
        }

        toolbarBinding?.toolbar?.addBackButton { getPresenter().onBackPressed() }
        toolbarBinding?.toolbar?.title = null

        placeholdersBinding?.networkErrorView?.apply {
            setText(R.string.common_error_message_without_pull)
            callback = { getPresenter().initData() }
            showButton()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(PREVIOUS_KEY, isPrevious)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
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
        fragmentBinding?.commentsLayout?.commentsMore?.text = text
    }

    //TODO holder and implementation for manga
    override fun setLinkedContent(linked: LinkedContent?) {
        val b = fragmentBinding ?: return
        context?.let { context ->
            b.linkedLayout.root.visibleIf { linked != null }
            if (linked != null) {
                b.linkedLayout.root.setOnClickListener { getPresenter().onContentClicked(linked.linkedType, linked.linkedId) }
                imageLoader.setImageWithPlaceHolder(b.linkedLayout.imageView, linked.imageUrl)
                b.linkedLayout.linkedTitleView.text = linked.linkedName

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
                        return String.format(context.getString(R.string.type_pattern_without_duration), type.type.uppercase(),
                                episodes.unknownIfZero())
                    }

                    val type = convertType(linked.type, linked.episodes)
                    val season = converter.convertAnimeSeasonToString(linked.dateAired)
                    val status = convertStatus(linked.status)

                    val typeText = context.getString(R.string.details_type).toBold().append(" ").append(type)
                    val seasonText = context.getString(R.string.details_season).toBold().append(" ").append(season)
                    val statusText = context.getString(R.string.details_status).toBold().append(" ").append(status)

                    b.linkedLayout.typeView.text = typeText
                    b.linkedLayout.seasonView.text = seasonText
                    b.linkedLayout.statusView.text = statusText
                } else if (linked is Manga) {
                    fun getLocalizedType(type: MangaType): String {
                        return when (type) {
                            MangaType.MANGA -> context.getString(R.string.type_manga_translatable)
                            MangaType.DOUJIN -> context.getString(R.string.type_doujin_translatable)
                            MangaType.MANHUA -> context.getString(R.string.type_manhua_translatable)
                            MangaType.MANHWA -> context.getString(R.string.type_manhwa_translatable)
                            MangaType.NOVEL -> context.getString(R.string.type_novel_translatable)
                            MangaType.LIGHT_NOVEL -> context.getString(R.string.type_novel_translatable)
                            MangaType.ONE_SHOT -> context.getString(R.string.type_one_shot_translatable)
                            MangaType.UNKNOWN -> ""
                        }
                    }

                    fun convertStatus(status: Status): String {
                        return when (status) {
                            Status.ANONS -> context.getString(R.string.status_anons)
                            Status.ONGOING -> context.getString(R.string.status_ongoing)
                            Status.RELEASED -> context.getString(R.string.status_released)
                            else -> context.getString(R.string.error_no_data)
                        }
                    }

                    fun convertType(type: MangaType, volumes: Int, chapters: Int): String {
                        val typeText = getLocalizedType(type)
                        val format = context.getString(R.string.type_pattern_manga)
                        return String.format(format, typeText, volumes.unknownIfZero(), chapters.unknownIfZero())
                    }



                    val type = convertType(linked.type, linked.volumes, linked.chapters)
                    val season = converter.convertAnimeSeasonToString(linked.dateAired)
                    val status = convertStatus(linked.status)

                    val typeText = context.getString(R.string.details_type).toBold().append(" ").append(type)
                    val seasonText = context.getString(R.string.details_season).toBold().append(" ").append(season)
                    val statusText = context.getString(R.string.details_status).toBold().append(" ").append(status)

                    b.linkedLayout.typeView.text = typeText
                    b.linkedLayout.seasonView.text = seasonText
                    b.linkedLayout.statusView.text = statusText

                }
            }
        }
    }

    override fun showCommentsLoading(show: Boolean) {
        val b = fragmentBinding ?: return
        b.commentsLayout.commentProgress.root.visibleIf { show }
        b.commentsLayout.recyclerView.visibleIf { !show }
    }

    override fun onShowLoading() {
        progressBinding?.progressBar?.visible()
    }

    override fun onHideLoading() {
        progressBinding?.progressBar?.gone()
    }

    override fun setCommentsCount(count: Long) {
        val b = fragmentBinding ?: return
        context?.let {
            val text = it.getString(R.string.common_comments) + " ($count):"
            b.commentsLayout.commentTitleView.text = text
        }
    }

    override fun showEmptyView() {
        commentsAdapter.showEmptyView()
    }

    override fun hideEmptyView() {
        commentsAdapter.hideEmptyView()
    }

    override fun showContent(show: Boolean) {
        fragmentBinding?.scrollView?.visibleIf { show }
    }

    override fun showCommentsMore(show: Boolean) {
        fragmentBinding?.commentsLayout?.commentsMore?.visibleIf { show }
    }

    override fun showPreviousComments(show: Boolean) {
        fragmentBinding?.commentsLayout?.commentsBefore?.visibleIf { show }
    }

}