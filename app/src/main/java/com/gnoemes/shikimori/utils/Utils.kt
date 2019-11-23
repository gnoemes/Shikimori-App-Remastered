package com.gnoemes.shikimori.utils

import android.graphics.Bitmap
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.domain.VideoHosting


object Utils {

    fun isHostingSupports(hosting: VideoHosting): Boolean {
        val supports = mutableListOf(
                VideoHosting.SIBNET,
                VideoHosting.VK,
                VideoHosting.SOVET_ROMANTICA,
                VideoHosting.SMOTRET_ANIME
        )

        return supports.contains(hosting)
    }

    fun getPriorityHosting(hostings: List<VideoHosting>): VideoHosting {
        return hostings.sortedBy { it.ordinal }.first()
    }

    fun getRequestHeadersForHosting(video: Video?): Map<String, String> = when {
        video?.hosting == VideoHosting.SOVET_ROMANTICA -> mapOf(Pair("Referer", video.player))
        else -> emptyMap()
    }

    fun getDominantColor(bitmap: Bitmap): Int {
        val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

}