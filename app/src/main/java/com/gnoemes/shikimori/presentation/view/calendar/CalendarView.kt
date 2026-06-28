package com.gnoemes.shikimori.presentation.view.calendar

import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CalendarView : BaseFragmentView {

    fun showData(items: List<CalendarViewModel>)

}