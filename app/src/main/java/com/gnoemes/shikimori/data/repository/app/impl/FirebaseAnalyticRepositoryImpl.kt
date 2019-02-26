package com.gnoemes.shikimori.data.repository.app.impl

import com.gnoemes.shikimori.data.repository.app.AnalyticRepository
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseAnalyticRepositoryImpl @Inject constructor(
        private val analytics: FirebaseAnalytics
) : AnalyticRepository {

    override fun logEvent(event: AnalyticEvent) = analytics.logEvent(event.toString(), null)

}