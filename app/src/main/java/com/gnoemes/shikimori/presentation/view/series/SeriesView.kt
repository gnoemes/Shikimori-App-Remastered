package com.gnoemes.shikimori.presentation.view.series

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.presentation.EpisodesNavigationData
import com.gnoemes.shikimori.entity.series.presentation.SeriesDownloadItem
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.presentation.presenter.common.AddToEndSingleTagStrategy
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface SeriesView : BaseFragmentView {

    fun showData(newItems: List<TranslationViewModel>)

    fun setEpisodeName(index: Int)

    fun changeSource(isAlternative : Boolean)

    fun setTranslationType(type: TranslationType)

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "search")
    fun showSearchView()

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "search")
    fun onSearchClosed()

    @StateStrategyType(SkipStrategy::class)
    fun showPlayerDialog()

    @StateStrategyType(SkipStrategy::class)
    fun showDownloadDialog(title : String, items: List<SeriesDownloadItem>)

    @StateStrategyType(SkipStrategy::class)
    fun showAuthorDialog(author: String)

    @StateStrategyType(SkipStrategy::class)
    fun showEpisodesDialog(data: EpisodesNavigationData)

    @StateStrategyType(SkipStrategy::class)
    fun checkPermissions()

    @StateStrategyType(SkipStrategy::class)
    fun showFolderChooserDialog()

    @StateStrategyType(SkipStrategy::class)
    fun showQualityChooser(items: List<Pair<String, String>>)

    fun showFab(show : Boolean)

    fun setBackground(image: Image)

    fun showEmptyAuthorsView(show: Boolean, isAlternative: Boolean = false)

    fun showEmptySearchView()

    @StateStrategyType(SkipStrategy::class)
    fun showTracksNotFoundError()

    @StateStrategyType(SkipStrategy::class)
    fun scrollToPosition(position: Int)

    fun showEpisodeLoading(show: Boolean)

    fun hideEpisodeName()

    fun showNextEpisode(show: Boolean)
}