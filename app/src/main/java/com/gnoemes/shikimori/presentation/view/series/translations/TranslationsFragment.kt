package com.gnoemes.shikimori.presentation.view.series.translations

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.entity.series.presentation.TranslationsNavigationData
import com.gnoemes.shikimori.presentation.presenter.series.translations.TranslationsPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.series.BaseSeriesFragment
import com.gnoemes.shikimori.presentation.view.series.translations.adapter.TranslationsAdapter
import com.gnoemes.shikimori.utils.*
import kotlinx.android.synthetic.main.fragment_base_series.*
import kotlinx.android.synthetic.main.fragment_translations.*
import kotlinx.android.synthetic.main.layout_toolbar_transparent.*
import kotlinx.android.synthetic.main.layout_translations_toolbar.*

class TranslationsFragment : BaseSeriesFragment<TranslationsPresenter, TranslationsView>(), TranslationsView {

    @InjectPresenter
    lateinit var translationsPresenter: TranslationsPresenter

    @ProvidePresenter
    fun providePresenter(): TranslationsPresenter {
        return presenterProvider.get().apply {
            localRouter = (parentFragment as RouterProvider).localRouter
            navigationData = arguments?.getParcelable(AppExtras.ARGUMENT_TRANSLATIONS_DATA)!!
        }
    }

    companion object {
        fun newInstance(data: TranslationsNavigationData) = TranslationsFragment().withArgs { putParcelable(AppExtras.ARGUMENT_TRANSLATIONS_DATA, data) }
    }

    private val adapter by lazy { TranslationsAdapter(getPresenter()::onHostingClicked, getPresenter()::onMenuClicked) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setTitle(R.string.translation_authors)
        with(recyclerView) {
            adapter = this@TranslationsFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        titleView.gone()
        fab.onClick { getPresenter().onDiscussionClicked() }

        translationBtn.onClick { showTypes(true) }
        voiceBtn.onClick { onTypeSelected(TranslationType.VOICE_RU) }
        subtitlesBtn.onClick { onTypeSelected(TranslationType.SUB_RU) }
        originalBtn.onClick { onTypeSelected(TranslationType.RAW) }
    }

    private fun onTypeSelected(newType: TranslationType) {
        getPresenter().onTypeChanged(newType)
        showTypes(false)
    }

    private fun showTypes(show: Boolean) {
        TransitionManager.beginDelayedTransition(motionLayout, AutoTransition())
        translationTypeLabel.visibleIf { !show }
        translationBtn.visibleIf { !show }
        voiceBtn.visibleIf { show }
        subtitlesBtn.visibleIf { show }
        originalBtn.visibleIf { show }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): TranslationsPresenter = translationsPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_translations

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(newItems: List<TranslationViewModel>) {
        adapter.bindItems(newItems)
    }

    override fun showContent(show: Boolean) {
        recyclerView.visibleIf { show }
    }

    override fun setEpisodeName(index: Int) {
        val text = String.format(context!!.getString(R.string.episode_number), index)
        translationToolbar.title = text
    }

    override fun setTranslationType(type: TranslationType) {
        val textAndIcon = when (type) {
            TranslationType.VOICE_RU -> Pair(R.string.translation_voice, R.drawable.ic_voice)
            TranslationType.SUB_RU -> Pair(R.string.translation_subtitles, R.drawable.ic_subs)
            TranslationType.RAW -> Pair(R.string.translation_original, R.drawable.ic_original)
            else -> null
        }

        textAndIcon?.let {
            translationTypeLabel.setText(it.first)
            translationBtn.setImageResource(it.second)
        }

    }

    override fun onShowLoading() {
        progress.visible()
    }

    override fun onHideLoading() {
        progress.gone()
    }
}