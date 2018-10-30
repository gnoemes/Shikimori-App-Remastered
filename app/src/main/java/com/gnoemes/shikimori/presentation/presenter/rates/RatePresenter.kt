package com.gnoemes.shikimori.presentation.presenter.rates

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.common.paginator.PageOffsetPaginator
import com.gnoemes.shikimori.presentation.presenter.common.paginator.ViewController
import com.gnoemes.shikimori.presentation.view.rates.RateView
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class RatePresenter @Inject constructor(
        private val ratesInteractor: RatesInteractor
) : BaseNetworkPresenter<RateView>(), ViewController<Rate> {

    var userId: Long = Constants.NO_ID
    lateinit var type: Type
    lateinit var rateStatus: RateStatus
    private lateinit var paginator: PageOffsetPaginator<Rate>
    private val isAnime: Boolean
        get() = type == Type.ANIME

    override fun initData() {
        paginator = PageOffsetPaginator({ loadRate(it) }, this)
        paginator.refresh()
    }

    private fun loadRate(page: Int): Single<List<Rate>> =
            if (isAnime) ratesInteractor.getAnimeRates(userId, page, Constants.MAX_LIMIT, rateStatus)
            else ratesInteractor.getMangaRates(userId, page, Constants.MAX_LIMIT, rateStatus)

    override fun showEmptyError(show: Boolean, throwable: Throwable?) {
        if (show) processErrors(throwable!!)
        else viewState.apply {
            hideEmptyView()
            hideNetworkView()
        }
    }

    override fun showEmptyView(show: Boolean) {
        if (show) viewState.showEmptyView()
        else viewState.hideEmptyView()
    }

    override fun showData(show: Boolean, data: List<Rate>) {
        if (show) viewState.showData(data)
        else viewState.hideList()
    }

    override fun showRefreshProgress(show: Boolean) {
        if (show) viewState.showRefresh()
        else viewState.hideRefresh()
    }

    override fun showPageProgress(show: Boolean) {
        viewState.showPageProgress(show)
    }

    override fun showError(throwable: Throwable) {
        processErrors(throwable)
    }

    fun onRefresh() {
        paginator.refresh()
    }

    fun loadNewPage() {
        paginator.loadNewPage()
    }

    fun onAction(it: DetailsAction) {
        when (it) {
            is DetailsAction.ChangeRateStatus -> onChangeRateStatus(it.id, it.newStatus)
            is DetailsAction.EditRate -> onEditRate(it.rate)
        }
    }

    private fun onEditRate(rate: Rate?) {
        val userRate = UserRate(
                id = rate?.id,
                targetId = rate?.anime?.id ?: rate?.manga?.id,
                targetType = type,
                score = rate?.score?.toDouble(),
                status = rateStatus,
                userId = userId,
                episodes = rate?.episodes,
                chapters = rate?.chapters,
                text = rate?.text
        )

        viewState.showRateDialog(userRate)
    }

    private fun onChangeRateStatus(id: Long, newStatus: RateStatus) =
            ratesInteractor.changeRateStatus(id, newStatus)
                    .subscribeAndRefresh()

    fun onDeleteRate(id: Long) =
            ratesInteractor.deleteRate(id)
                    .subscribeAndRefresh()


    fun onUpdateRate(rate: UserRate) =
            ratesInteractor.updateRate(rate)
                    .subscribeAndRefresh()

    private fun Completable.subscribeAndRefresh() {
        this.subscribe(this@RatePresenter::onRefresh, this@RatePresenter::processErrors)
                .addToDisposables()

    }
}


