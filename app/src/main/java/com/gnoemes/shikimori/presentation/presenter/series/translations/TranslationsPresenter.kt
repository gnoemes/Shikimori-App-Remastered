package com.gnoemes.shikimori.presentation.presenter.series.translations

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.entity.series.domain.TranslationMenu
import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.entity.series.presentation.TranslationsNavigationData
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.series.translations.converter.TranslationsViewModelConverter
import com.gnoemes.shikimori.presentation.view.series.translations.TranslationsView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class TranslationsPresenter @Inject constructor(
        private val interactor: SeriesInteractor,
        private val settingsSource: SettingsSource,
        private val converter: TranslationsViewModelConverter
) : BaseNetworkPresenter<TranslationsView>() {

    lateinit var navigationData: TranslationsNavigationData

    private var setting: TranslationSetting? = null

    override fun initData() {
        super.initData()
        viewState.setEpisodeName(navigationData.episodeIndex)
        viewState.setBackground(navigationData.image)

        loadData()
    }

    private fun loadData() {
        if (setting == null && settingsSource.useLocalTranslationSettings) {
            interactor.getTranslationSettings(navigationData.animeId, navigationData.episodeIndex)
                    .doOnSuccess { setting = it }
                    .flatMap {loadTranslations(it.lastType ?: settingsSource.translationType)}
                    .subscribeToTranslations()

        }
        else loadTranslations(settingsSource.translationType).subscribeToTranslations()
    }

    private fun loadTranslations(type: TranslationType) =
            interactor.getTranslations(type, navigationData.animeId, navigationData.episodeId, navigationData.isAlternative)
                    .map { converter.convertTranslations(it, setting) }

    private fun setData(data: List<TranslationViewModel>) {
        val items = data.toMutableList()
        if (items.find { it.isSameAuthor } != null) {
            val priorityItem = items[items.indexOfFirst { it.isSameAuthor }]
            items.remove(priorityItem)
            items.add(0, priorityItem)
        }
        viewState.showData(items)
    }

    fun onHostingClicked(hosting: TranslationVideo) {
        //TODO show choose player dialog or show video
    }

    fun onMenuClicked(category: TranslationMenu) {
        //TODO handle item's menu clicks
    }

    fun onDiscussionClicked() {
        //TODO find related topic (can be parsed from episode link or topic api)
    }

    private fun Single<List<TranslationViewModel>>.subscribeToTranslations() {
        this.appendLoadingLogic(viewState)
                .subscribe(this@TranslationsPresenter::setData, this@TranslationsPresenter::processErrors)
                .addToDisposables()
    }
}