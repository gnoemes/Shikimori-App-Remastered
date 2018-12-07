package com.gnoemes.shikimori.presentation.view.anime

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.anime.AnimePresenter
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.adapter.GenreAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.content.ContentAdapter
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.RateDialogFragment
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsContentViewHolder
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsDescriptionViewHolder
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsHeadViewHolder
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsOptionsViewHolder
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_anime.*
import kotlinx.android.synthetic.main.layout_collapsing_toolbar.*
import javax.inject.Inject


class AnimeFragment : BaseFragment<AnimePresenter, AnimeView>(), AnimeView,
        ListDialogFragment.DialogCallback, ListDialogFragment.DialogIdCallback, RateDialogFragment.RateDialogCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var settings: SettingsSource

    @Inject
    lateinit var resourceProvider: RatingResourceProvider

    @InjectPresenter
    lateinit var animePresenter: AnimePresenter

    @ProvidePresenter
    fun providePresenter(): AnimePresenter {
        animePresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            animePresenter.localRouter = (parentFragment as RouterProvider).localRouter
        }

        arguments.ifNotNull {
            animePresenter.id = it.getLong(AppExtras.ARGUMENT_ANIME_ID)
        }

        return animePresenter
    }

    companion object {
        fun newInstance(id: Long) = AnimeFragment().withArgs { putLong(AppExtras.ARGUMENT_ANIME_ID, id) }
    }

    private val onOffsetChangedListener = AppBarLayout.OnOffsetChangedListener { appbar, verticalOffset ->

        fun isCollapsed(): Boolean {
            return Math.abs(verticalOffset) >= appbar.totalScrollRange
        }
        appbar.post {
            if (isCollapsed()) showToolbar()
            else hideToolbar()
        }
    }

    private lateinit var headHolder: DetailsHeadViewHolder
    private lateinit var descriptionHolder: DetailsDescriptionViewHolder
    private lateinit var optionsHolder: DetailsOptionsViewHolder
    private val contentHolders = HashMap<DetailsContentType, DetailsContentViewHolder>()

    private val genreAdapter by lazy { GenreAdapter(getPresenter()::onAction) }

    private val videoAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val charactersAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val similarAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val relatedAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getFragmentLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            addBackButton()
            title = null
            setNavigationOnClickListener { getPresenter().onBackPressed() }
        }

        val params = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = AppBarLayout.Behavior().apply {
            setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })

        }
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener)

        headHolder = DetailsHeadViewHolder(headLayout, imageLoader, resourceProvider, genreAdapter, getPresenter()::onAction)
        descriptionHolder = DetailsDescriptionViewHolder(descriptionLayout)
        optionsHolder = DetailsOptionsViewHolder(optionsLayout, getPresenter()::onAction)

        contentHolders.apply {
            put(DetailsContentType.VIDEO, DetailsContentViewHolder(videoLayout, videoAdapter))
            put(DetailsContentType.CHARACTERS, DetailsContentViewHolder(charactersLayout, charactersAdapter))
            put(DetailsContentType.SIMILAR, DetailsContentViewHolder(similarLayout, similarAdapter))
            put(DetailsContentType.RELATED, DetailsContentViewHolder(relatedLayout, relatedAdapter))
        }
    }

    private fun showToolbar() {
        toolbar?.apply {
            setTitle(R.string.common_anime)
            background = ColorDrawable(context.colorAttr(R.attr.colorPrimary))
        }
    }

    private fun hideToolbar() {
        toolbar?.apply {
            title = null
            background = ColorDrawable(Color.TRANSPARENT)
        }
    }

    override fun dialogItemIdCallback(tag: String?, id: Long) {
        getPresenter().onAnimeClicked(id)
    }

    override fun dialogItemCallback(tag: String?, url: String) {
        getPresenter().onOpenWeb(url)
    }

    override fun onUpdateRate(rate: UserRate) {
        getPresenter().onUpdateOrCreateRate(rate)
    }

    override fun onDeleteRate(id: Long) {
        getPresenter().onDeleteRate(id)
    }

    override fun onDestroyView() {
        appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener)
        super.onDestroyView()
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): AnimePresenter = animePresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_anime

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setHeadItem(item: DetailsHeadItem) {
        headHolder.bind(item)

        if (!backgroundImage.hasImage()) {
            imageLoader.setImageWithPlaceHolder(backgroundImage, item.image.original)
        }
    }

    override fun setOptionsItem(item: DetailsOptionsItem) {
        optionsHolder.bind(item)
    }

    override fun setDescriptionItem(item: DetailsDescriptionItem) {
        descriptionHolder.bind(item)
    }

    override fun setContentItem(type: DetailsContentType, item: DetailsContentItem) {
        contentHolders[type]?.bind(type, item)
    }

    override fun setEpisodes(items: List<Any>) {
    }

    override fun showRateDialog(userRate: UserRate?) {
        val dialog = RateDialogFragment.newInstance(rate = userRate)
        dialog.show(childFragmentManager, "RateTag")
    }

    override fun showLinks(it: List<Pair<String, String>>) {
        val dialog = ListDialogFragment.newInstance()
        dialog.apply {
            setTitle(R.string.common_links)
            setItems(it)
        }.show(childFragmentManager, "LinksTag")
    }

    override fun showChronology(it: List<Pair<String, String>>) {
        val dialog = ListDialogFragment.newInstance(true)
        dialog.apply {
            setTitle(R.string.common_chronology)
            setItems(it)
        }.show(childFragmentManager, "ChronologyTag")
    }

    override fun showEpisodeLoading() {
    }

    override fun hideEpisodeLoading() {
    }

}