package com.gnoemes.shikimori.entity.common.presentation.shikimori

import com.gnoemes.shikimori.utils.network.RuntimeTypeAdapterFactory
import com.google.gson.GsonBuilder

object ShikimoriViews {
    const val DELIMITER = "$"
    const val START_SYMBOL = "{"
    const val END_SYMBOL = "}"
    const val ACTION_DIVIDER = "_"

    private val adapterFactory = RuntimeTypeAdapterFactory.of(Content::class.java, "contentType", true)
            .apply {
                registerSubtype(Link::class.java, "link")
                registerSubtype(Reply::class.java, "reply")
            }

    private val gson = GsonBuilder()
            .registerTypeAdapterFactory(adapterFactory)
            .create()

    fun deserializeContent(content: String): Content {
        return if (content.startsWith(ShikimoriViews.START_SYMBOL)) try {
            gson.fromJson<Content>(content, Content::class.java)
        } catch (err: Exception) {
            err.printStackTrace()
            return Text(content)
        }
        else Text(content)
    }
}