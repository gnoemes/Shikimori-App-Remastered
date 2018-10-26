package com.gnoemes.shikimori.presentation.view.anime

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.anime.AnimePresenter
import com.gnoemes.shikimori.presentation.view.anime.adapter.AnimeAdapter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.RateDialogFragment
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.ViewStatePagerAdapter
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class AnimeFragment : BaseFragment<AnimePresenter, AnimeView>(), AnimeView,
        ListDialogFragment.DialogCallback, ListDialogFragment.DialogIdCallback, RateDialogFragment.RateDialogCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var settings: SettingsSource

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

    private val animeAdapter by lazy { AnimeAdapter(imageLoader, getPresenter()::onAction, getPresenter()::onContentClicked, settings) }

    private lateinit var animeRecyclerView: RecyclerView
    private lateinit var episodesRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            addBackButton()
            setTitle(R.string.common_anime)
            setNavigationOnClickListener { getPresenter().onBackPressed() }
        }

        animeRecyclerView = layoutInflater.inflate(R.layout.fragment_anime, null) as RecyclerView
        pageContainerView.adapter = PageAdapter(listOf(animeRecyclerView))
        pageContainerView.offscreenPageLimit = 2

        with(animeRecyclerView) {
            adapter = animeAdapter
            layoutManager = LinearLayoutManager(context).apply { initialPrefetchItemCount = 5 }
            setHasFixedSize(true)
//            layoutManager = FlexboxLayoutManager(context, FlexDirection.COLUMN)
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

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): AnimePresenter = animePresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_details

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setAnime(items: List<Any>) {
        animeAdapter.bindItems(items)
    }

    override fun setEpisodes(items: List<Any>) {
    }

    override fun updateCharacters(it: Any) = animeAdapter.updateCharacters(it)

    override fun updateSimilar(it: Any) = animeAdapter.updateSimilar(it)

    override fun updateRelated(it: Any) = animeAdapter.updateRelated(it)

    override fun updateHead(it: Any) = animeAdapter.updateHead(it)

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

    internal class PageAdapter(val pages: List<View>) : ViewStatePagerAdapter() {

        override fun createView(container: ViewGroup?, position: Int): View {
            return pages[position]
        }

        override fun getCount(): Int {
            return pages.count()
        }

    }

}