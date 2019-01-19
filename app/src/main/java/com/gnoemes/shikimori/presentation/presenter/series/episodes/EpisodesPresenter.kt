package com.gnoemes.shikimori.presentation.presenter.series.episodes

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.entity.series.presentation.EpisodesNavigationData
import com.gnoemes.shikimori.presentation.presenter.anime.converter.EpisodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.series.episodes.EpisodesView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class EpisodesPresenter @Inject constructor(
        private val interactor: SeriesInteractor,
        private val converter: EpisodeViewModelConverter
) : BaseNetworkPresenter<EpisodesView>() {

    lateinit var navigationData: EpisodesNavigationData
    private var rateId = Constants.NO_ID

    override fun initData() {
        super.initData()
        loadData()
        rateId = navigationData.rateId ?: rateId

        viewState.setBackground(navigationData.image)
        viewState.setName(navigationData.name)
    }

    private fun loadData() =
            interactor.getEpisodes(navigationData.animeId)
                    .appendLoadingLogic(viewState)
                    .map(converter)
                    .subscribe(this::setData, this::processErrors)
                    .addToDisposables()

    private fun setData(items: List<EpisodeViewModel>) {
        //TODO scroll to last - 1 watched episode. Ignore gaps.
        //TODO states for episode items [unchecked, loading, checked]
        viewState.showData(items)
    }

    fun onEpisodeClicked(item: EpisodeViewModel) {
        //TODO
        router.navigateTo(Screens.TRANSLATIONS)
    }

    fun onEpisodeStatusChanged(item: EpisodeViewModel, newStatus: Boolean) {
        //TODO rewrite, add something like debouncer
        //TODO rateId creation logic
        if (newStatus) {
            interactor.setEpisodeWatched(item.animeId, item.id, rateId)
                    .subscribe(this::loadData, this::processErrors)
                    .addToDisposables()
        } else {
            if (rateId == Constants.NO_ID) {
                //TODO get rate id and decrement
            }
            interactor.setEpisodeUnwatched(item.animeId, item.id, rateId)
                    .subscribe(this::loadData, this::processErrors)
                    .addToDisposables()

        }
    }
}