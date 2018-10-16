package com.gnoemes.shikimori.presentation.presenter.calendar.converter

import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import io.reactivex.functions.Function

interface CalendarViewModelConverter : Function<List<CalendarItem>, List<CalendarViewModel>>