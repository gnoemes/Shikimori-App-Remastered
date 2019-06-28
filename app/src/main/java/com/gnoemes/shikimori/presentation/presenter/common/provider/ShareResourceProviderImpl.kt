package com.gnoemes.shikimori.presentation.presenter.common.provider

import android.content.Context
import com.gnoemes.shikimori.R
import javax.inject.Inject

class ShareResourceProviderImpl @Inject constructor(
        private val context: Context
) : ShareResourceProvider {

    override fun getEpisodeShareFormattedMessage(title: String, episode: Int, url: String): String {
        return "$title \n" +
                String.format(context.getString(R.string.episode_number), episode) +
                " • " +
                context.getString(R.string.app_name) + "\n\n" +
                url
    }

}