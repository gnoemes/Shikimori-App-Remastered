package com.gnoemes.shikimori.utils

import android.graphics.Bitmap
import android.webkit.CookieManager
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.domain.VideoHosting


object Utils {

    fun hostingFromString(raw: String?): VideoHosting {
        return when (raw) {
            "vk.com", "vk" -> VideoHosting.VK()
            "ok.ru", "ok" -> VideoHosting.OK()
            "www.myvi.top", "www.myvi.tv", "myvi.top", "myvi.tv" -> VideoHosting.MYVI()
            "csst.online", "www.csst.online", "fsst.online", "www.fsst.online", "secvideo1.online", "www.secvideo1.online" -> VideoHosting.ALLVIDEO()
            "animejoy.ru" -> VideoHosting.ANIMEJOY()
            "dzen.ru" -> VideoHosting.DZEN()
            "nuum.ru" -> VideoHosting.NUUM()
            "my.mail.ru", "videoapi.my.mail.ru", "mail.ru" -> VideoHosting.MAILRU()
            "video.sibnet.ru", "sibnet", "sibnet.ru" -> VideoHosting.SIBNET()
            "sovetromantica.com", "sovetromantica" -> VideoHosting.SOVET_ROMANTICA()
            "smotretanime.ru", "smotretanime", "smotret-anime.online", "smotret-anime.com" -> VideoHosting.SMOTRET_ANIME()
            "aniqit.com" -> VideoHosting.KODIK()
            else -> (raw ?: "unknown").let { hosting -> VideoHosting.UNKNOWN(hosting, hosting) }
        }
    }

    fun isHostingSupports(hosting: VideoHosting): Boolean {
        return when (hosting) {
            is VideoHosting.SIBNET, is VideoHosting.VK, is VideoHosting.SMOTRET_ANIME, is VideoHosting.SOVET_ROMANTICA, is VideoHosting.KODIK, is VideoHosting.OK, is VideoHosting.MYVI, is VideoHosting.ALLVIDEO, is VideoHosting.ANIMEJOY, is VideoHosting.DZEN, is VideoHosting.NUUM, is VideoHosting.MAILRU -> true
            else -> false
        }
    }

    fun getRequestHeadersForHosting(video: Video?): Map<String, String> = when (video?.hosting) {
        is VideoHosting.SOVET_ROMANTICA, is VideoHosting.UNKNOWN -> mapOf(Pair("Referrer", video.player))
        is VideoHosting.SIBNET -> mapOf(Pair("Referer", video.player))
        is VideoHosting.MAILRU -> mapOf(Pair("Cookie", CookieManager.getInstance().getCookie(".my.mail.ru")))
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