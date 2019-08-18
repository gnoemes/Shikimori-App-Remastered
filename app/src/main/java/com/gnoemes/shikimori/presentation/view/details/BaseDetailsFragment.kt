package com.gnoemes.shikimori.presentation.view.details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.presenter.details.BaseDetailsPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.common.adapter.ActionAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.InfoAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.TagAdapter
import com.gnoemes.shikimori.presentation.view.common.fragment.EditRateFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.LinkDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.common.holders.*
import com.gnoemes.shikimori.presentation.view.rates.status.RateStatusDialog
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.layout_collapsing_toolbar.*
import kotlinx.android.synthetic.main.layout_details_content_with_search.view.*
import javax.inject.Inject

abstract class BaseDetailsFragment<Presenter : BaseDetailsPresenter<View>, View : BaseDetailsView> : BaseFragment<Presenter, View>(),
        BaseDetailsView, ListDialogFragment.DialogCallback, ListDialogFragment.DialogIdCallback, EditRateFragment.RateDialogCallback, RateStatusDialog.RateStatusCallback, LinkDialogFragment.LinkCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var resourceProvider: RatingResourceProvider

    private var detailsName: String? = null

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
            detailsName?.let { title = it } ?: setTitle(titleRes)
            background = ColorDrawable(context.colorAttr(R.attr.colorPrimary))
        }
    }

    protected open fun hideToolbar() {
        toolbar?.apply {
            title = null
            background = ColorDrawable(Color.TRANSPARENT)
        }
    }

    protected open lateinit var headHolder: DetailsHeadViewHolder
    protected open lateinit var infoHolder: DetailsInfoViewHolder
    protected open lateinit var actionHolder: DetailsActionViewHolder
    protected open lateinit var descriptionHolder: DetailsDescriptionViewHolder

    protected open val contentHolders = HashMap<DetailsContentType, DetailsContentViewHolder>()

    protected open val tagAdapter by lazy { TagAdapter(getPresenter()::onAction) }
    protected open val infoAdapter by lazy { InfoAdapter(getPresenter()::onContentClicked) }
    protected open val actionAdapter by lazy { ActionAdapter(getPresenter()::onAction) }

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

        headHolder = DetailsHeadViewHolder(headLayout, imageLoader, getPresenter()::onAction)
        infoHolder = DetailsInfoViewHolder(infoLayout, tagAdapter, infoAdapter)
        actionHolder = DetailsActionViewHolder(actionLayout, actionAdapter)
        descriptionHolder = DetailsDescriptionViewHolder(descriptionLayout, getPresenter()::onContentClicked)

        with(charactersLayout.searchView) {
            findViewById<LinearLayout>(R.id.search_bar)?.layoutTransition = null
            setOnCloseListener {
                getPresenter().onCharacterSearch(null)
                true
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideSoftInput()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    getPresenter().onCharacterSearch(newText)
                    return true
                }
            })
            findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)?.apply {
                setPadding(context.dp(16), 0, context.dp(8), 0)
                setHintTextColor(context.colorStateList(context.attr(R.attr.colorOnPrimarySecondary).resourceId))
            }
            findViewById<LinearLayout>(R.id.search_edit_frame)?.apply {
                layoutParams = (layoutParams as? LinearLayout.LayoutParams)?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        marginStart = 0
                    }; leftMargin = 0
                }
            }
        }

        with(charactersLayout) {
            searchBtn.onClick {
                searchView.isIconified = false
                searchView.post {
                    TransitionManager.beginDelayedTransition(this@with as ViewGroup, Fade())
                    contentLabelView.gone()
                    searchBtn.gone()
                    searchView.visible()
                    closeBtn.visible()
                }
            }
            closeBtn.onClick {
                TransitionManager.beginDelayedTransition(this@with as ViewGroup, Fade())
                contentLabelView.visible()
                searchBtn.visible()
                searchView.setQuery("", false)
                searchView.gone()
                closeBtn.gone()
                searchView.isIconified = true
            }
        }
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

    override fun onStatusChanged(id: Long, newStatus: RateStatus) {
        getPresenter().onChangeRateStatus(newStatus)
    }

    override fun onLinkAction(action: DetailsAction.Link) {
        getPresenter().onAction(action)
    }

    override fun onDestroyView() {
        appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener)
        super.onDestroyView()
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getFragmentLayout(): Int = R.layout.fragment_details

    abstract val titleRes: Int

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setHeadItem(item: DetailsHeadItem) {
        detailsName = item.name
        headHolder.bind(item)

        if (!backgroundImage.hasImage()) {
            imageLoader.setBlurredImage(backgroundImage, item.image.original, sampling = 2)
        }
    }

    override fun setInfoItem(item: DetailsInfoItem) {
        infoHolder.bind(item)
    }

    override fun setDescriptionItem(item: DetailsDescriptionItem) {
        descriptionHolder.bind(item)
    }

    override fun setActionItem(item: DetailsActionItem) {
        actionHolder.bind(item)
    }

    override fun setContentItem(type: DetailsContentType, item: DetailsContentItem) {
        contentHolders[type]?.bind(type, item)
    }

    override fun showLinks(it: List<Link>) {
        val dialog = LinkDialogFragment.newInstance(it)
        dialog.show(childFragmentManager, "LinksTag")
    }

    override fun showStatusDialog(id: Long, name: String, currentStatus: RateStatus?, isAnime: Boolean) {
        hideSoftInput()
        val dialog = RateStatusDialog.newInstance(id, name, currentStatus, isAnime)
        dialog.show(childFragmentManager, "StatusDialog")
    }


}