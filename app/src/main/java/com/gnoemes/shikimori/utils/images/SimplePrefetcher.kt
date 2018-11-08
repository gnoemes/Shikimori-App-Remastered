package com.gnoemes.shikimori.utils.images

import android.content.Context

class SimplePrefetcher(private val context: Context) : Prefetcher {

    override fun prefetch(urls: List<String?>) {
        urls.forEach {
            GlideApp.with(context).downloadOnly().load(it)
        }
    }
}