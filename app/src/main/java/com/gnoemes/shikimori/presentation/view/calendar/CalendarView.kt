package com.gnoemes.shikimori.presentation.view.calendar

import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface CalendarView : BaseFragmentView {

    fun showData(items: List<CalendarViewModel>)

}