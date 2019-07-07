package com.gnoemes.shikimori.presentation.presenter.calendar

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.calendar.CalendarInteractor
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.calendar.converter.CalendarViewModelConverter
import com.gnoemes.shikimori.presentation.view.calendar.CalendarView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import com.gnoemes.shikimori.utils.applySingleSchedulers
import com.gnoemes.shikimori.utils.clearAndAddAll
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class CalendarPresenter @Inject constructor(
        private val interactor: CalendarInteractor,
        private val converter: CalendarViewModelConverter
) : BaseNetworkPresenter<CalendarView>() {

    private val items = mutableListOf<CalendarItem>()
    private var query: String? = null

    override fun initData() {
        loadData()
    }

    private fun loadData() {
        interactor.getCalendarData()
                .appendLoadingLogic(viewState)
                .doOnSuccess { items.clearAndAddAll(it) }
                .subscribe(this::convertAndSet, this::processErrors)
                .addToDisposables()
    }

    private fun convertAndSet(items: List<CalendarItem>) {
        Single.just(items)
                .map(converter)
                .applySingleSchedulers(Schedulers.single())
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }

    private fun setData(items: List<CalendarViewModel>) {
        if (items.isEmpty() && !query.isNullOrBlank()) viewState.showEmptyView()
        else viewState.showData(items)
    }

    fun onRefresh() {
        loadData()
    }

    fun onQueryChanged(newText: String?) {
        this.query = newText

        if (query.isNullOrBlank()) {
            convertAndSet(items)
        } else {
            val searchItems = items.filter {
                it.anime.name.contains(query ?: "", true)
                        || it.anime.nameRu?.contains(query ?: "", true) != false
            }
            convertAndSet(searchItems)
        }
    }
}

