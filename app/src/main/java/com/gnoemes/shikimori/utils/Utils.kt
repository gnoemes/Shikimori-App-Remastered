package com.gnoemes.shikimori.utils

import android.graphics.Bitmap
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.domain.VideoHosting


object Utils {

    fun hostingFromString(raw: String?): VideoHosting {
        return when (raw) {
            "vk.com", "vk" -> VideoHosting.VK()
            "video.sibnet.ru", "sibnet", "sibnet.ru" -> VideoHosting.SIBNET()
            "sovetromantica.com", "sovetromantica" -> VideoHosting.SOVET_ROMANTICA()
            "smotretanime.ru", "smotretanime", "smotret-anime.online" -> VideoHosting.SMOTRET_ANIME()
            "aniqit.com" -> VideoHosting.KODIK()
            else -> (raw ?: "unknown").let { hosting -> VideoHosting.UNKNOWN(hosting, hosting) }
        }
    }

    fun isHostingSupports(hosting: VideoHosting): Boolean {
        return when (hosting) {
            is VideoHosting.SIBNET, is VideoHosting.VK, is VideoHosting.SMOTRET_ANIME, is VideoHosting.SOVET_ROMANTICA, is VideoHosting.KODIK -> true
            else -> false
        }
    }

    fun getRequestHeadersForHosting(video: Video?): Map<String, String> = when (video?.hosting) {
        is VideoHosting.SOVET_ROMANTICA, is VideoHosting.UNKNOWN -> mapOf(Pair("Referrer", video.player))
        is VideoHosting.SIBNET -> mapOf(Pair("Referer", video.player))
        else -> emptyMap()
    }

    fun getDominantColor(bitmap: Bitmap): Int {
        val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

    fun checkNeedIFrame(url: String): Boolean {
        return when {
            url.contains("aparat") -> false
            else -> true
        }
    }

}