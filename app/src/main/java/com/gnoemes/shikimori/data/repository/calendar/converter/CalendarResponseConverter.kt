package com.gnoemes.shikimori.data.repository.calendar.converter

import com.gnoemes.shikimori.entity.calendar.data.CalendarResponse
import com.gnoemes.shikimori.entity.calendar.domain.CalendarItem
import io.reactivex.functions.Function

interface CalendarResponseConverter : Function<List<CalendarResponse>, List<CalendarItem>>