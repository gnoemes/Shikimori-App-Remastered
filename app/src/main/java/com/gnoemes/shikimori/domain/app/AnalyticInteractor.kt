package com.gnoemes.shikimori.domain.app

import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent

interface AnalyticInteractor {

    fun logEvent(event : AnalyticEvent)

}