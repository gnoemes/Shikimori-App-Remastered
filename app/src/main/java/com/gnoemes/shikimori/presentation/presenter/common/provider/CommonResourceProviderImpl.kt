package com.gnoemes.shikimori.presentation.presenter.common.provider

import android.content.Context
import com.gnoemes.shikimori.R
import javax.inject.Inject

class CommonResourceProviderImpl @Inject constructor(
        private val context: Context
) : CommonResourceProvider {

    override val emptyMessage: String
        get() = context.getString(R.string.common_empty)

    override val needAuth: String
        get() = context.getString(R.string.common_need_auth)

    override val needAuthRates: String
        get() = context.getString(R.string.rates_need_auth)

    override val topicNotFound: String
        get() = context.getString(R.string.common_topic_not_found)
}