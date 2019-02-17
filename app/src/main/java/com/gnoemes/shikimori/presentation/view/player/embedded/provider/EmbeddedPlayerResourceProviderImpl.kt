package com.gnoemes.shikimori.presentation.view.player.embedded.provider

import android.content.Context
import com.gnoemes.shikimori.R
import javax.inject.Inject

class EmbeddedPlayerResourceProviderImpl @Inject constructor(
        private val context: Context
) : EmbeddedPlayerResourceProvider {

    override val hostingErrorMessage: String
        get() = context.getString(R.string.player_hosting_error)
    override val playerErrorMessage: String
        get() = context.getString(R.string.player_error)
}