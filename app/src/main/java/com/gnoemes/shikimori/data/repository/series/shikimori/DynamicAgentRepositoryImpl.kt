package com.gnoemes.shikimori.data.repository.series.shikimori

import android.content.Context
import com.gnoemes.shikimori.utils.getDefaultSharedPreferences
import javax.inject.Inject

class DynamicAgentRepositoryImpl @Inject constructor(
        private val context: Context
) : DynamicAgentRepository {

    companion object {
        private const val defaultAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0"
    }

    override fun getAgent(): String = context.getDefaultSharedPreferences().getString("agent", defaultAgent) ?: defaultAgent
}