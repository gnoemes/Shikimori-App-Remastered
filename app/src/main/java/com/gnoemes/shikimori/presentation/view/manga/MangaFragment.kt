package com.gnoemes.shikimori.presentation.view.manga

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
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.entity.manga.presentation.MangaNavigationData
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.presenter.manga.MangaPresenter
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
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.layout_collapsing_toolbar.*
import javax.inject.Inject

class MangaFragment : BaseFragment<MangaPresenter, MangaView>(), MangaView,
        ListDialogFragment.DialogCallback, ListDialogFragment.DialogIdCallback, RateDialogFragment.RateDialogCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var resourceProvider: RatingResourceProvider

    @InjectPresenter
    lateinit var mangaPresenter: MangaPresenter

    @ProvidePresenter
    fun providePresenter(): MangaPresenter =
            presenterProvider.get().apply {
                localRouter = (parentFragment as RouterProvider).localRouter
                val data = arguments?.getParcelable<MangaNavigationData>(AppExtras.ARGUMENT_MANGA_DATA)!!
                id = data.id
                isRanobe = data.type == Type.RANOBE
            }

    companion object {
        fun newInstance(data: MangaNavigationData) = MangaFragment().withArgs { putParcelable(AppExtras.ARGUMENT_MANGA_DATA, data) }
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

    private val charactersAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val similarAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val relatedAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getFragmentLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            addBackButton { getPresenter().onBackPressed() }
            title = null
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
        videoLayout.gone()

        headHolder = DetailsHeadViewHolder(headLayout, imageLoader, resourceProvider, genreAdapter, getPresenter()::onAction)
        descriptionHolder = DetailsDescriptionViewHolder(descriptionLayout, getPresenter()::onContentClicked)
        optionsHolder = DetailsOptionsViewHolder(optionsLayout, getPresenter()::onAction)

        contentHolders.apply {
            put(DetailsContentType.CHARACTERS, DetailsContentViewHolder(charactersLayout, charactersAdapter))
            put(DetailsContentType.SIMILAR, DetailsContentViewHolder(similarLayout, similarAdapter))
            put(DetailsContentType.RELATED, DetailsContentViewHolder(relatedLayout, relatedAdapter))
        }
    }

    private fun showToolbar() {
        toolbar?.apply {
            setTitle(R.string.common_manga)
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
        getPresenter().onMangaClicked(id)
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

    override fun getPresenter(): MangaPresenter = mangaPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_details

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

    override fun showRateDialog(userRate: UserRate?) {
        val dialog = RateDialogFragment.newInstance(rate = userRate, isAnime = false)
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
            setTitle(R.string.common_chronology_read)
            setItems(it)
        }.show(childFragmentManager, "ChronologyTag")
    }

}