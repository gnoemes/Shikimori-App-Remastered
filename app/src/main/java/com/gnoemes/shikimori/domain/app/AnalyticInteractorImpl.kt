package com.gnoemes.shikimori.domain.app

import com.gnoemes.shikimori.data.repository.app.AnalyticRepository
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import javax.inject.Inject

class AnalyticInteractorImpl @Inject constructor(
        private val repository: AnalyticRepository
) : AnalyticInteractor {

    override fun logEvent(event: AnalyticEvent) = repository.logEvent(event)

}