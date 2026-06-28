package com.gnoemes.shikimori.utils.images

import android.content.Context
import com.bumptech.glide.Glide

class SimplePrefetcher(private val context: Context) : Prefetcher {

    override fun prefetch(urls: List<String?>) {
        urls.forEach {
            Glide.with(context).downloadOnly().load(it)
        }
    }
}