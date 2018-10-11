package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.calendar.data.CalendarResponse
import io.reactivex.Single
import retrofit2.http.GET

interface CalendarApi {

    @GET("/api/calendar")
    fun getCalendar(): Single<List<CalendarResponse>>
}