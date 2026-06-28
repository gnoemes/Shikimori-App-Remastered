package com.gnoemes.shikimori.presentation.view.anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentDetailsBinding
import com.gnoemes.shikimori.databinding.LayoutDetailsContentBinding
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.anime.AnimePresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.adapter.content.ContentAdapter
import com.gnoemes.shikimori.presentation.view.common.fragment.EditRateFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsContentViewHolder
import com.gnoemes.shikimori.presentation.view.details.BaseDetailsFragment
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.onMenuClick
import com.gnoemes.shikimori.utils.withArgs

class AnimeFragment : BaseDetailsFragment<AnimePresenter, AnimeView>(), AnimeView {

    private var _detailsBinding: FragmentDetailsBinding? = null
    private val detailsBinding: FragmentDetailsBinding? get() = _detailsBinding

    @InjectPresenter
    lateinit var animePresenter: AnimePresenter

    @ProvidePresenter
    fun providePresenter(): AnimePresenter =
            presenterProvider.get().apply {
                localRouter = (parentFragment as RouterProvider).localRouter
                id = arguments!!.getLong(AppExtras.ARGUMENT_ANIME_ID)
            }

    companion object {
        fun newInstance(id: Long) = AnimeFragment().withArgs { putLong(AppExtras.ARGUMENT_ANIME_ID, id) }
    }

    private val videoAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val charactersAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val screenshotsAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val relatedAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _detailsBinding = FragmentDetailsBinding.bind(view)

        collapsingToolbarBinding?.toolbar?.apply {
            inflateMenu(R.menu.menu_anime)
            onMenuClick {
                when (it?.itemId) {
                    R.id.item_rate -> getPresenter().onAction(DetailsAction.RateStatusDialog)
                    R.id.item_web -> getPresenter().onAction(DetailsAction.OpenInBrowser)
                    R.id.item_share -> getPresenter().onAction(DetailsAction.Share)
                }
                true
            }
        }

        contentHolders.apply {
            put(DetailsContentType.VIDEO, DetailsContentViewHolder(detailsBinding!!.videoLayout, videoAdapter))
            put(DetailsContentType.CHARACTERS, DetailsContentViewHolder(LayoutDetailsContentBinding.bind(detailsBinding!!.charactersLayout.root), charactersAdapter, true))
            put(DetailsContentType.SCREENSHOTS, DetailsContentViewHolder(detailsBinding!!.screenshotsLayout, screenshotsAdapter))
            put(DetailsContentType.RELATED, DetailsContentViewHolder(detailsBinding!!.relatedLayout, relatedAdapter))
        }

        with(detailsBinding!!.actionBtn) {
            setImageResource(R.drawable.ic_play_arrow_filled)
            onClick { getPresenter().onAction(DetailsAction.WatchOnline()) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _detailsBinding = null
    }

    override fun dialogItemIdCallback(tag: String?, id: Long) {
        getPresenter().onAnimeClicked(id)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): AnimePresenter = animePresenter

    override val titleRes: Int
        get() = R.string.common_anime

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showRateDialog(title: String, userRate: UserRate?) {
        val dialog = EditRateFragment.newInstance(rate = userRate, title = title)
        dialog.show(childFragmentManager, "RateTag")
    }

    override fun showChronology(it: List<Pair<String, String>>) {
        val dialog = ListDialogFragment.newInstance(true)
        dialog.apply {
            setTitle(R.string.common_chronology)
            setItems(it)
        }.show(childFragmentManager, "ChronologyTag")
    }
}