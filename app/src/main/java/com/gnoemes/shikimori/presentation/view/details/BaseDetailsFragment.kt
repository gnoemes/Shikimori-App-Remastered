package com.gnoemes.shikimori.presentation.view.details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentDetailsBinding
import com.gnoemes.shikimori.databinding.LayoutCollapsingToolbarBinding
import com.gnoemes.shikimori.databinding.LayoutDetailsContentWithSearchBinding
import com.gnoemes.shikimori.utils.colorAttr
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.presenter.details.BaseDetailsPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.common.adapter.ActionAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.InfoAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.TagAdapter
import com.gnoemes.shikimori.presentation.view.common.fragment.EditRateFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.LinkDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.StatisticDialogFragment
import com.gnoemes.shikimori.presentation.view.common.holders.*
import com.gnoemes.shikimori.presentation.view.rates.status.RateStatusDialog
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.google.android.material.appbar.AppBarLayout
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

abstract class BaseDetailsFragment<Presenter : BaseDetailsPresenter<View>, View : BaseDetailsView> : BaseFragment<Presenter, View>(),
        BaseDetailsView, ListDialogFragment.DialogCallback, ListDialogFragment.DialogIdCallback, EditRateFragment.RateDialogCallback, RateStatusDialog.RateStatusCallback, LinkDialogFragment.LinkCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var resourceProvider: RatingResourceProvider

    private var detailsName: String? = null

    private var fragmentBinding: FragmentDetailsBinding? = null
    protected var collapsingToolbarBinding: LayoutCollapsingToolbarBinding? = null
    private var charactersBinding: LayoutDetailsContentWithSearchBinding? = null

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
        collapsingToolbarBinding?.toolbar?.apply {
            detailsName?.let { title = it } ?: setTitle(titleRes)
            background = ColorDrawable(context.colorAttr(R.attr.colorPrimary))
        }
    }

    protected open fun hideToolbar() {
        collapsingToolbarBinding?.toolbar?.apply {
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
    protected open val infoAdapter by lazy { InfoAdapter(getPresenter()::onContentClicked, imageLoader) }
    protected open val actionAdapter by lazy { ActionAdapter(getPresenter()::onAction) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View {
        initBaseBinding(inflater, container)

        fragmentBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        val fb = requireNotNull(fragmentBinding) { "fragmentBinding was null after inflate" }
        val appBarLayout = fb.root.findViewById<AppBarLayout>(R.id.included_layout_collapsing_toolbar)
        collapsingToolbarBinding = LayoutCollapsingToolbarBinding.bind(requireNotNull(appBarLayout) { "appBarLayout was null" })
        charactersBinding = fb.charactersLayout
        return fb.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collapsingToolbarBinding?.toolbar?.apply {
            addBackButton { getPresenter().onBackPressed() }
            title = null
        }

        val ctb = requireNotNull(collapsingToolbarBinding) { "collapsingToolbarBinding was null in onViewCreated" }
        val fb = requireNotNull(fragmentBinding) { "fragmentBinding was null in onViewCreated" }
        val cb = requireNotNull(charactersBinding) { "charactersBinding was null in onViewCreated" }

        val params = ctb.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = AppBarLayout.Behavior().apply {
            setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })

        }
        ctb.appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener)

        headHolder = DetailsHeadViewHolder(ctb.headLayout, imageLoader, getPresenter()::onAction)
        infoHolder = DetailsInfoViewHolder(fb.infoLayout, tagAdapter, infoAdapter)
        actionHolder = DetailsActionViewHolder(fb.actionLayout, actionAdapter)
        descriptionHolder = DetailsDescriptionViewHolder(fb.descriptionLayout, getPresenter()::onContentClicked)

        with(cb.searchView) {
            val searchBarId = context.resources.getIdentifier("search_bar", "id", "android")
            val searchSrcTextId = context.resources.getIdentifier("search_src_text", "id", "android")
            val searchEditFrameId = context.resources.getIdentifier("search_edit_frame", "id", "android")
            
            findViewById<LinearLayout>(searchBarId)?.layoutTransition = null
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
            findViewById<SearchView.SearchAutoComplete>(searchSrcTextId)?.apply {
                setPadding(context.dp(16), 0, context.dp(8), 0)
                setHintTextColor(context.colorStateList(context.attr(R.attr.colorOnPrimarySecondary).resourceId))
            }
            findViewById<LinearLayout>(searchEditFrameId)?.apply {
                layoutParams = (layoutParams as? LinearLayout.LayoutParams)?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        marginStart = 0
                    }; leftMargin = 0
                }
            }
        }

        with(cb) {
            searchBtn.onClick {
                searchView.isIconified = false
                searchView.post {
                    TransitionManager.beginDelayedTransition(this@with.root, Fade())
                    contentLabelView.gone()
                    searchBtn.gone()
                    searchView.visible()
                    closeBtn.visible()
                }
            }
            closeBtn.onClick {
                TransitionManager.beginDelayedTransition(root, Fade())
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
        collapsingToolbarBinding?.appBarLayout?.removeOnOffsetChangedListener(onOffsetChangedListener)
        super.onDestroyView()
        fragmentBinding = null
        collapsingToolbarBinding = null
        charactersBinding = null
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getFragmentLayout(): Int = View.NO_ID

    abstract val titleRes: Int

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setHeadItem(item: DetailsHeadItem) {
        detailsName = item.name
        headHolder.bind(item)

        fragmentBinding?.let { fb ->
            if (!fb.backgroundImage.hasImage()) {
                imageLoader.setBlurredImage(fb.backgroundImage, item.image.original, sampling = 2)
            }
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

    override fun showStatistic(title: String, scores: List<UserStatisticItem>, rates: List<UserStatisticItem>) {
        hideSoftInput()
        val dialog = StatisticDialogFragment.newInstance(title, scores, rates)
        dialog.show(childFragmentManager, "StatisticDialog")
    }

    override fun openInBrowser(url: String) {
        context?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}