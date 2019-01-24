package com.gnoemes.shikimori.presentation.view.series.episodes

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.series.presentation.EpisodePlaceholderItem
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.entity.series.presentation.EpisodesNavigationData
import com.gnoemes.shikimori.entity.series.presentation.SeriesPlaceholderItem
import com.gnoemes.shikimori.presentation.presenter.series.episodes.EpisodesPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.series.BaseSeriesFragment
import com.gnoemes.shikimori.presentation.view.series.episodes.adapter.EpisodeAdapter
import com.gnoemes.shikimori.utils.dimen
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.visibleIf
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.gnoemes.shikimori.utils.withArgs
import com.lapism.searchview.SearchView
import kotlinx.android.synthetic.main.fragment_base_series.*
import kotlinx.android.synthetic.main.fragment_episodes.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_episode_error_placeholder.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class EpisodesFragment : BaseSeriesFragment<EpisodesPresenter, EpisodesView>(), EpisodesView, ListDialogFragment.DialogCallback {

    @InjectPresenter
    lateinit var episodesPresenter: EpisodesPresenter

    @ProvidePresenter
    fun providePresenter(): EpisodesPresenter {
        return presenterProvider.get().apply {
            localRouter = (parentFragment as RouterProvider).localRouter
            navigationData = arguments?.getParcelable(AppExtras.ARGUMENT_EPISODES_DATA)!!
        }
    }

    companion object {
        fun newInstance(data: EpisodesNavigationData) = EpisodesFragment().withArgs { putParcelable(AppExtras.ARGUMENT_EPISODES_DATA, data) }
        private const val CHECK_ALL_PREVIOUS_ACTION = "check_all_previous_"
    }

    private val adapter by lazy { EpisodeAdapter(getPresenter()::onEpisodeClicked, getPresenter()::onEpisodeStatusChanged, getPresenter()::onEpisodeLongClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureSearchView()

        toolbar?.apply {
            inflateMenu(R.menu.menu_episodes)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_alternative_source -> getPresenter().onAlternativeSourceClicked()
                    R.id.item_search -> getPresenter().onSearchClicked()
                }
                true
            }
        }

        with(recyclerView) {
            adapter = this@EpisodesFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dimen(R.dimen.margin_normal).toInt(), true))
            setHasFixedSize(true)
        }

        emptyContentView.setText(R.string.episodes_not_found)
        networkErrorView.showButton()
        networkErrorView.callback = { getPresenter().onRefresh() }
        altBtnView.setOnClickListener { getPresenter().onAlternativeSourceClicked() }
        fab.gone()
    }

    private fun configureSearchView() {
        (searchView.findViewById<CardView>(R.id.cardView).layoutParams as FrameLayout.LayoutParams).setMargins(0, 0, 0, 0)
        searchView.setHeight(56f)
        searchView.setArrowOnly(true)
        searchView.shouldClearOnClose = true
        searchView.inputType = InputType.TYPE_CLASS_NUMBER
        val defaultPadding = context?.dimen(R.dimen.margin_big)?.toInt() ?: 0
        searchView.findViewById<LinearLayout>(R.id.linearLayout).setPadding(0, 0, defaultPadding, 0)
        searchView.findViewById<ImageView>(R.id.imageView_arrow_back).apply {
            (layoutParams as LinearLayout.LayoutParams).apply { width = LinearLayout.LayoutParams.WRAP_CONTENT; height = LinearLayout.LayoutParams.WRAP_CONTENT }
            setPadding(defaultPadding, defaultPadding, 0, defaultPadding)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.close(true)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getPresenter().onQueryChanged(newText)
                return true
            }
        })

        searchView.setOnOpenCloseListener(object : SearchView.OnOpenCloseListener {
            var first = true
            override fun onOpen(): Boolean = true

            override fun onClose(): Boolean {
                if (!first) getPresenter().onSearchClosed()
                first = false
                return true
            }
        })
    }

    override fun dialogItemCallback(tag: String?, action: String) {
        if (!tag.isNullOrBlank() && tag == "OptionsTag") {
            if (action.contains(CHECK_ALL_PREVIOUS_ACTION)) getPresenter().onCheckAllPrevious(action.replace(CHECK_ALL_PREVIOUS_ACTION, "").toInt())
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): EpisodesPresenter = episodesPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_episodes

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(newItems: List<EpisodeViewModel>) {
        adapter.bindItems(newItems)
    }

    override fun scrollToPosition(position: Int) {
        (recyclerView?.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(position, 0)
    }

    override fun showSearchView() {
        searchView.open(true)
    }

    override fun onShowLoading() {
        val items = (1..12).map { EpisodePlaceholderItem(it) }
        adapter.bindItems(items)
    }

    override fun showLicencedError(show: Boolean) {
        placeholderContainer.visibleIf { show }
        errorTextView.setText(R.string.episodes_under_license)
    }

    override fun showBlockedError(show: Boolean) {
        placeholderContainer.visibleIf { show }
        errorTextView.setText(R.string.episodes_blocked)
    }

    override fun showAlternativeLabel(show: Boolean) {
        alternativeLabel.visibleIf { show }
    }

    override fun showEpisodeOptionsDialog(index: Int) {
        val dialog = ListDialogFragment.newInstance()
        val items = mutableListOf<Pair<String, String>>()
                .apply {
                    add(Pair(context!!.getString(R.string.episode_check_all_previous), CHECK_ALL_PREVIOUS_ACTION + index))
                }
        val title = String.format(context!!.getString(R.string.episode_number), index)
        dialog.apply {
            setTitle(title)
            setItems(items)
        }.show(childFragmentManager, "OptionsTag")
    }

    override fun onHideLoading() {}

    override fun showContent(show: Boolean) = recyclerView.visibleIf { show }

    override fun showSearchEmpty() {
        val item = SeriesPlaceholderItem(R.string.episode_search_empty_title, R.string.episode_search_empty_desc)
        adapter.bindItems(mutableListOf(item))
    }
}