package com.gnoemes.shikimori.presentation.presenter.calendar

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.domain.calendar.CalendarInteractor
import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.ContentException
import com.gnoemes.shikimori.entity.app.domain.exceptions.NetworkException
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarPage
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.calendar.converter.CalendarViewModelConverter
import com.gnoemes.shikimori.presentation.view.calendar.CalendarView
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class CalendarPresenter @Inject constructor(
        private val interactor: CalendarInteractor,
        private val converter: CalendarViewModelConverter,
        private val settingsSource: SettingsSource
) : BaseNetworkPresenter<CalendarView>() {

    override fun initData() {
        loadData()
        loadMyCalendar()

        if (settingsSource.isMyOngoingPriority) viewState.setPage(CalendarPage.MY_ONGOINGS)
    }

    private fun loadMyCalendar() {
        val page = CalendarPage.MY_ONGOINGS
        interactor.getMyCalendarData()
                .appendLoadingLogic(page)
                .map(converter)
                .subscribe({ setOngoings(page, it) }, this::processMyOngoings)
                .addToDisposables()
    }

    private fun loadData() {
        val page = CalendarPage.ALL
        interactor.getCalendarData()
                .appendLoadingLogic(page)
                .map(converter)
                .subscribe({ setOngoings(page, it) }, this::processErrors)
                .addToDisposables()
    }

    fun onRefreshMyOngoings() {
        loadMyCalendar()
    }

    fun onRefresh() {
        loadData()
    }

    private fun processMyOngoings(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            NetworkException.TAG -> {
                viewState.hideOngoings(CalendarPage.MY_ONGOINGS)
                viewState.onShowNetworkError(CalendarPage.MY_ONGOINGS)
            }
            ContentException.TAG -> {
                viewState.hideOngoings(CalendarPage.MY_ONGOINGS)
                viewState.onShowEmptyView(CalendarPage.MY_ONGOINGS)
            }
            else -> super.processErrors(throwable)
        }
    }

    override fun processErrors(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            NetworkException.TAG -> {
                viewState.hideOngoings(CalendarPage.ALL)
                viewState.onShowNetworkError(CalendarPage.ALL)
            }
            else -> super.processErrors(throwable)
        }
    }

    private fun setOngoings(page: CalendarPage, items: List<CalendarViewModel>) {
        if (items.isNotEmpty()) {
            viewState.showOngoings(page, items)
        } else {
            viewState.hideOngoings(page)
            viewState.onShowEmptyView(page)
        }
    }

    private fun <T> Single<T>.appendLoadingLogic(page: CalendarPage): Single<T> =
            this.doOnSubscribe { viewState.onShowOngoingsLoading(page) }
                    .doOnSubscribe { viewState.onHideNetworkError(page) }
                    .doOnSubscribe { viewState.onHideEmptyView(page) }
                    .doAfterTerminate { viewState.onHideOngoingsLoading(page) }
                    .doOnEvent { _, _ -> viewState.onHideOngoingsLoading(page) }
}

