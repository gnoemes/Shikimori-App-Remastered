package com.gnoemes.shikimori.data.repository.app

import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent

interface AnalyticRepository {

    fun logEvent(event : AnalyticEvent)
}