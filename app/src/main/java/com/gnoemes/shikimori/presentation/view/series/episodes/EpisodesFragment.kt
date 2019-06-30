package com.gnoemes.shikimori.presentation.view.series.episodes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.series.presentation.EpisodePlaceholderItem
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.entity.series.presentation.EpisodesNavigationData
import com.gnoemes.shikimori.entity.series.presentation.SeriesPlaceholderItem
import com.gnoemes.shikimori.presentation.presenter.series.episodes.EpisodesPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetInjectionDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.series.episodes.adapter.EpisodeAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import kotlinx.android.synthetic.main.fragment_episodes.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*

class EpisodesFragment : BaseBottomSheetInjectionDialogFragment<EpisodesPresenter, EpisodesView>(), EpisodesView, ListDialogFragment.DialogCallback {

    @InjectPresenter
    lateinit var episodesPresenter: EpisodesPresenter

    @ProvidePresenter
    fun providePresenter(): EpisodesPresenter {
        return presenterProvider.get().apply {
            navigationData = arguments?.getParcelable(AppExtras.ARGUMENT_EPISODES_DATA)!!
        }
    }

    companion object {
        fun newInstance(data: EpisodesNavigationData) = EpisodesFragment().withArgs { putParcelable(AppExtras.ARGUMENT_EPISODES_DATA, data) }
        private const val CHECK_ALL_PREVIOUS_ACTION = "check_all_previous_"
    }

    private val adapter by lazy { EpisodeAdapter(presenter::onEpisodeClicked, presenter::onEpisodeStatusChanged, presenter::onEpisodeLongClick) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(getDialogLayout(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureSearchView()

        toolbar?.apply {
            addBackButton(R.drawable.ic_close) { onBackPressed() }
            setTitle(R.string.common_episodes)
            inflateMenu(R.menu.menu_episodes)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_alternative_source -> presenter.onAlternativeSourceClicked()
                    R.id.item_search -> presenter.onSearchClicked()
                }
                true
            }
        }

        searchToolbar.addBackButton { presenter.onSearchClosed() }

        with(recyclerView) {
            adapter = this@EpisodesFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dimen(R.dimen.margin_normal).toInt(), true))
            setHasFixedSize(true)
        }

        emptyContentView.setText(R.string.episodes_not_found)
        networkErrorView.showButton()
        networkErrorView.callback = { presenter.onRefresh() }
        emptyContentView.gone()
        networkErrorView.gone()
    }

    private fun configureSearchView() {
        with(searchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideSoftInput()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    presenter.onQueryChanged(newText)
                    return true
                }
            })
            findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)?.apply {
                setPadding(0, 0, context.dp(8), 0)
                setHintTextColor(AppCompatResources.getColorStateList(context, context.attr(R.attr.colorOnPrimarySecondary).resourceId))
            }
            findViewById<LinearLayout>(R.id.search_edit_frame)?.apply {
                layoutParams = (layoutParams as? LinearLayout.LayoutParams)?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        marginStart = 0
                    }; leftMargin = 0
                }
            }
            findViewById<ImageView>(R.id.search_close_btn)?.apply {
                setPadding(context!!.dp(12), 0, context!!.dp(12), 0)
                tint(context.colorAttr(R.attr.colorOnPrimary))
            }
        }
    }

    override fun dialogItemCallback(tag: String?, action: String) {
        if (!tag.isNullOrBlank() && tag == "OptionsTag") {
            if (action.contains(CHECK_ALL_PREVIOUS_ACTION)) presenter.onCheckAllPrevious(action.replace(CHECK_ALL_PREVIOUS_ACTION, "").toInt())
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override val presenter: EpisodesPresenter
        get() = episodesPresenter

    override fun getDialogLayout(): Int = R.layout.fragment_episodes

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(newItems: List<EpisodeViewModel>) {
        adapter.bindItems(newItems)
    }

    override fun scrollToPosition(pos: Int) {
        (recyclerView?.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(pos, 0)
    }

    override fun showSearchView() {
        TransitionManager.beginDelayedTransition(appBarLayout, Fade())
        searchToolbar.visible()
        toolbar.gone()
        searchView.isIconified = false
    }

    override fun hideSearchView() {
        TransitionManager.beginDelayedTransition(appBarLayout, Fade())
        searchToolbar.gone()
        toolbar.visible()
        hideSoftInput()
    }

    override fun onShowLoading() {
        val items = (1..12).map { EpisodePlaceholderItem(it) }
        adapter.bindItems(items)
    }

    override fun showAlternativeLabel(show: Boolean) {
        if (show) toolbar.setSubtitle(R.string.series_alternative_source)
        else toolbar.subtitle = null
        toolbar.setTitleTextAppearance(context, if (show) R.style.ToolbarSmallTextAppearance else R.style.ToolbarTextAppearance)
    }

    override fun showEpisodeOptionsDialog(index: Int) {
        val dialog = ListDialogFragment.newInstance()
        val items = mutableListOf<Pair<String, String>>()
                .apply {
                    add(Pair(context!!.getString(R.string.episode_check_all_previous) + " $index", CHECK_ALL_PREVIOUS_ACTION + index))
                }
        dialog.apply {
            setItems(items)
        }.show(childFragmentManager, "OptionsTag")
    }

    override fun onHideLoading() = Unit
    override fun showContent(show: Boolean) = Unit

    override fun showSearchEmpty() {
        val item = SeriesPlaceholderItem(R.string.episode_search_empty_title, R.string.episode_search_empty_desc)
        adapter.bindItems(mutableListOf(item))
    }

    override fun onEpisodeSelected(episodeId: Long, episode: Int, isAlternative: Boolean) {
        (parentFragment as? EpisodesCallback)?.onEpisodeSelected(episodeId, episode, isAlternative)
        onBackPressed()
    }

    override fun onRateCreated(id: Long) {
        (parentFragment as? EpisodesCallback)?.onRateCreated(id)
    }

    override fun showSystemMessage(message: String) {
        Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
    }

    interface EpisodesCallback {
        fun onRateCreated(id: Long)
        fun onEpisodeSelected(episodeId: Long, episode: Int, isAlternative: Boolean)
    }
}