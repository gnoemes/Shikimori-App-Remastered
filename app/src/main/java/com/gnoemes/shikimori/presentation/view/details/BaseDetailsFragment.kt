package com.gnoemes.shikimori.presentation.view.details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.presenter.details.BaseDetailsPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.common.adapter.GenreAdapter
import com.gnoemes.shikimori.presentation.view.common.fragment.EditRateFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsContentViewHolder
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsDescriptionViewHolder
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsHeadViewHolder
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsOptionsViewHolder
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.colorAttr
import com.gnoemes.shikimori.utils.hasImage
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.layout_collapsing_toolbar.*
import javax.inject.Inject

abstract class BaseDetailsFragment<Presenter : BaseDetailsPresenter<View>, View : BaseDetailsView> : BaseFragment<Presenter, View>(),
        BaseDetailsView, ListDialogFragment.DialogCallback, ListDialogFragment.DialogIdCallback, EditRateFragment.RateDialogCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var resourceProvider: RatingResourceProvider

    protected open val onOffsetChangedListener = AppBarLayout.OnOffsetChangedListener { appbar, verticalOffset ->

        fun isCollapsed(): Boolean {
            return Math.abs(verticalOffset) >= appbar.totalScrollRange
        }
        appbar.post {
            if (isCollapsed()) showToolbar()
            else hideToolbar()
        }
    }

    protected open fun showToolbar() {
        toolbar?.apply {
            setTitle(titleRes)
            background = ColorDrawable(context.colorAttr(R.attr.colorPrimary))
        }
    }

    protected open fun hideToolbar() {
        toolbar?.apply {
            title = null
            background = ColorDrawable(Color.TRANSPARENT)
        }
    }

    protected lateinit var headHolder: DetailsHeadViewHolder
    protected lateinit var descriptionHolder: DetailsDescriptionViewHolder
    protected lateinit var optionsHolder: DetailsOptionsViewHolder

    protected val contentHolders = HashMap<DetailsContentType, DetailsContentViewHolder>()

    protected val genreAdapter by lazy { GenreAdapter(getPresenter()::onAction) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(getFragmentLayout(), container, false)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
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

        headHolder = DetailsHeadViewHolder(headLayout, imageLoader, resourceProvider, genreAdapter, getPresenter()::onAction)
        descriptionHolder = DetailsDescriptionViewHolder(descriptionLayout, getPresenter()::onContentClicked)
        optionsHolder = DetailsOptionsViewHolder(optionsLayout, getPresenter()::onAction)
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

    override fun getFragmentLayout(): Int = R.layout.fragment_details

    abstract val titleRes : Int

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

    override fun showLinks(it: List<Pair<String, String>>) {
        val dialog = ListDialogFragment.newInstance()
        dialog.apply {
            setTitle(R.string.common_links)
            setItems(it)
        }.show(childFragmentManager, "LinksTag")
    }




}