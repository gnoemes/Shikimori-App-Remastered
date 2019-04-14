package com.gnoemes.shikimori.presentation.view.manga

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.manga.presentation.MangaNavigationData
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.manga.MangaPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.adapter.content.ContentAdapter
import com.gnoemes.shikimori.presentation.view.common.fragment.EditRateFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsContentViewHolder
import com.gnoemes.shikimori.presentation.view.details.BaseDetailsFragment
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.layout_details_options_content.*

class MangaFragment : BaseDetailsFragment<MangaPresenter, MangaView>(), MangaView{

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

    private val charactersAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val similarAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val relatedAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoLayout.gone()

        contentHolders.apply {
            put(DetailsContentType.CHARACTERS, DetailsContentViewHolder(charactersLayout, charactersAdapter))
            put(DetailsContentType.SIMILAR, DetailsContentViewHolder(similarLayout, similarAdapter))
            put(DetailsContentType.RELATED, DetailsContentViewHolder(relatedLayout, relatedAdapter))
        }

        watchOnlineBtn.isEnabled = false
    }

    override fun dialogItemIdCallback(tag: String?, id: Long) {
        getPresenter().onMangaClicked(id)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): MangaPresenter = mangaPresenter

    override val titleRes: Int
        get() = R.string.common_manga

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showRateDialog(userRate: UserRate?) {
        val dialog = EditRateFragment.newInstance(rate = userRate, isAnime = false)
        dialog.show(childFragmentManager, "RateTag")
    }

    override fun showChronology(it: List<Pair<String, String>>) {
        val dialog = ListDialogFragment.newInstance(true)
        dialog.apply {
            setTitle(R.string.common_chronology_read)
            setItems(it)
        }.show(childFragmentManager, "ChronologyTag")
    }

}