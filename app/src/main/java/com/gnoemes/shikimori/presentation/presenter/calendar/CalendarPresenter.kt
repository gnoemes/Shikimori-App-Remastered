package com.gnoemes.shikimori.presentation.presenter.calendar

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.domain.calendar.CalendarInteractor
import com.gnoemes.shikimori.entity.app.domain.exceptions.BaseException
import com.gnoemes.shikimori.entity.app.domain.exceptions.NetworkException
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.calendar.converter.CalendarViewModelConverter
import com.gnoemes.shikimori.presentation.view.calendar.CalendarView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class CalendarPresenter @Inject constructor(
        private val interactor: CalendarInteractor,
        private val converter: CalendarViewModelConverter
) : BaseNetworkPresenter<CalendarView>() {

    private var isMyOngoings = false

    override fun initData() {
        if (isMyOngoings) {
            loadMyCalendar()
            viewState.setTitle(R.string.calendar_my_ongoings)
        } else {
            loadData()
            viewState.setTitle(R.string.common_calendar)
        }
    }

    private fun loadMyCalendar() {
        interactor.getMyCalendarData()
                .appendLoadingLogic(viewState)
                .map(converter)
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }

    private fun loadData() {
        interactor.getCalendarData()
                .appendLoadingLogic(viewState)
                .map(converter)
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }

    fun onRefresh() {
        initData()
    }

    fun onSwitchFilter() {
        isMyOngoings = !isMyOngoings
        onRefresh()
    }

    private fun setData(items: List<CalendarViewModel>) {
        if (items.isNotEmpty()) {
            viewState.showItems(items)
        } else {
            viewState.hideList()
            viewState.showEmptyView()
        }
    }

    override fun processErrors(throwable: Throwable) {
        when ((throwable as? BaseException)?.tag) {
            NetworkException.TAG -> viewState.showNetworkView()
            else -> super.processErrors(throwable)
        }
    }


}