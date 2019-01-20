package com.gnoemes.shikimori.presentation.view.series.episodes

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.series.presentation.EpisodePlaceholderItem
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.entity.series.presentation.EpisodesNavigationData
import com.gnoemes.shikimori.presentation.presenter.series.episodes.EpisodesPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.series.BaseSeriesFragment
import com.gnoemes.shikimori.presentation.view.series.episodes.adapter.EpisodeAdapter
import com.gnoemes.shikimori.utils.dimen
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.fragment_base_series.*
import kotlinx.android.synthetic.main.fragment_episodes.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class EpisodesFragment : BaseSeriesFragment<EpisodesPresenter, EpisodesView>(), EpisodesView {

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
    }

    private val adapter by lazy { EpisodeAdapter(getPresenter()::onEpisodeClicked, getPresenter()::onEpisodeStatusChanged) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            inflateMenu(R.menu.menu_episodes)
            setOnMenuItemClickListener {
                when(it.itemId) {
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
    }

    override fun onShowLoading() {
        val items = (1..12).map { EpisodePlaceholderItem(it) }
        adapter.bindItems(items)
    }

    override fun onHideLoading() {}

    override fun showContent(show: Boolean) = Unit
}