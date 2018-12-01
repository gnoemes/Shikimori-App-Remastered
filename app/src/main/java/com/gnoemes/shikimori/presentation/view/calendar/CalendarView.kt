package com.gnoemes.shikimori.presentation.view.calendar

import com.gnoemes.shikimori.entity.calendar.presentation.CalendarPage
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface CalendarView : BaseFragmentView {

    fun showOngoings(calendarPage: CalendarPage, items: List<CalendarViewModel>)

    fun hideOngoings(calendarPage: CalendarPage)

    fun onShowOngoingsLoading(calendarPage: CalendarPage)

    fun onHideOngoingsLoading(calendarPage: CalendarPage)

    fun onShowNetworkError(calendarPage: CalendarPage)

    fun onHideNetworkError(calendarPage: CalendarPage)

    fun onShowEmptyView(calendarPage: CalendarPage)

    fun onHideEmptyView(calendarPage: CalendarPage)

}