package com.gnoemes.shikimori.presentation.presenter.manga

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.manga.MangaInteractor
import com.gnoemes.shikimori.domain.ranobe.RanobeInteractor
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.domain.related.RelatedInteractor
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.*
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.user.domain.Statistic
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.converter.FranchiseNodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.presenter.details.BaseDetailsPresenter
import com.gnoemes.shikimori.presentation.presenter.manga.converter.MangaDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.view.manga.MangaView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import com.gnoemes.shikimori.utils.applySingleSchedulers
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

@InjectViewState
class MangaPresenter @Inject constructor(
        private val mangaInteractor: MangaInteractor,
        private val ranobeInteractor: RanobeInteractor,
        private val relatedInteractor: RelatedInteractor,
        private val detailsConverter: MangaDetailsViewModelConverter,
        private val settingsSource: SettingsSource,
        ratesInteractor: RatesInteractor,
        userInteractor: UserInteractor,
        resourceProvider: CommonResourceProvider,
        nodeConverter: FranchiseNodeViewModelConverter,
        contentConverter: DetailsContentViewModelConverter
) : BaseDetailsPresenter<MangaView>(ratesInteractor, userInteractor, resourceProvider, nodeConverter, contentConverter) {

    var isRanobe: Boolean = false

    private lateinit var currentManga: MangaDetails

    override val type: Type
        get() = if (isRanobe) Type.RANOBE else Type.MANGA

    override fun loadContent(showLoading: Boolean): Single<DetailsHeadItem> =
            loadDetails()
                    .flatMap {
                        if (showLoading) Single.just(it).appendLoadingLogic(viewState)
                        else Single.just(it)
                    }
                    .doOnSuccess { loadInfo() }
                    .doOnSuccess { loadActions() }
                    .doOnSuccess { loadDescription() }

    override fun loadDetails(): Single<DetailsHeadItem> =
            (if (isRanobe) ranobeInteractor.getDetails(id)
            else mangaInteractor.getDetails(id))
                    .doOnSuccess { currentManga = it; rateId = it.userRate?.id ?: Constants.NO_ID }
                    .map { detailsConverter.convertHead(it, userId == Constants.NO_ID) }

    override val characterFactory: (id: Long) -> Single<Roles>
        get() = {
            (if (isRanobe) ranobeInteractor.getRoles(it)
            else mangaInteractor.getRoles(it))
        }

    override val relatedFactory: (id: Long) -> Single<List<Related>>
        get() = {
            (if (isRanobe) relatedInteractor.getRanobe(it)
            else relatedInteractor.getManga(it))
        }

    override val linkFactory: (id: Long) -> Single<List<Link>>
        get() = {
            (if (isRanobe) ranobeInteractor.getLinks(it)
            else mangaInteractor.getLinks(it))
        }

    private fun loadInfo() {
        val item = detailsConverter.convertInfo(currentManga, creators)
        viewState.setInfoItem(item)
    }

    private fun loadActions() {
        val item = detailsConverter.getActions()
        viewState.setActionItem(item)
    }

    private fun loadDescription() {
        val descriptionItem = detailsConverter.convertDescriptionItem(currentManga.description)
        viewState.setDescriptionItem(descriptionItem)
    }


    override fun setCreators(creators: List<Pair<Person, List<String>>>) {
        super.setCreators(creators)
        val item = detailsConverter.convertInfo(currentManga, creators)
        viewState.setInfoItem(item)
    }

    override fun onOpenDiscussion() {
        currentManga.topicId?.let { onTopicClicked(it) }
                ?: router.showSystemMessage(resourceProvider.topicNotFound)
    }

    override fun onSimilarClicked() {
        super.onSimilarClicked()
        val data = CommonNavigationData(currentManga.id, Type.MANGA)
        router.navigateTo(Screens.SIMILAR, data)
    }

    override fun onEditRate() {
        viewState.showRateDialog(title, currentManga.userRate)
    }

    override fun onStatusDialog() {
        viewState.showStatusDialog(id, title, currentManga.userRate?.status, false)
    }

    override fun onOpenInBrowser() = onOpenWeb(currentManga.url)

    override fun onStatisticClicked() {
        Single.zip(Single.just(currentManga.rateScoresStats), Single.just(currentManga.rateStatusesStats), BiFunction<List<Statistic>, List<Statistic>, Pair<List<UserStatisticItem>, List<UserStatisticItem>>> { t1, t2 ->
            val scores = detailsConverter.convertScores(t1)
            val statuses = detailsConverter.convertStatuses(t2)
            Pair(scores, statuses)
        })
                .applySingleSchedulers()
                .subscribe({ viewState.showStatistic(title, it.first, it.second) }, this::processErrors)
                .addToDisposables()
    }

    override fun onWatchOnline() {
        //TODO chapters screen
    }

    private val title: String
        get() = if (settingsSource.isRussianNaming) currentManga.nameRu
                ?: currentManga.name else currentManga.name
}