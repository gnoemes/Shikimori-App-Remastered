package com.gnoemes.shikimori.data.repository.series.shikimori.parser

import android.webkit.CookieManager
import com.gnoemes.shikimori.entity.series.data.MailRuVideosResponse
import com.gnoemes.shikimori.entity.series.data.MailRuPlayerData
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.google.gson.Gson
import org.jsoup.Jsoup
import retrofit2.Response
import java.net.HttpCookie
import javax.inject.Inject

class MailRuParserImpl @Inject constructor() : MailRuParser {

    override fun video(video: TranslationVideo, tracks: List<Track>): Video =
            Video(video.animeId, video.episodeIndex.toLong(), video.webPlayerUrl!!, video.videoHosting, tracks, null, null)

    override fun parseVideoMetaUrl(html: String?): String? {
        if (html.isNullOrEmpty()) return null

        val doc = Jsoup.parse(html)
        val playerDataJson = doc
                .select("script:containsData(flashVars):containsData(video):containsData(metadataUrl)")
                .first()
                .data()

        if (playerDataJson.isNullOrEmpty()) return null

        val gson = Gson()
        val playerData = gson.fromJson<MailRuPlayerData>(playerDataJson, MailRuPlayerData::class.java)

        return "https://my.mail.ru${playerData.video.metadataUrl}"
    }

    override fun tracks(videosMetadata: MailRuVideosResponse?): List<Track> {
        return videosMetadata
                ?.videos
                ?.map {
                    val quality = it.key.replace("p", "")
                    val url = if (it.url.startsWith("http")) it.url else "https:${it.url}"

                    Track(quality, url)
                }
                ?.sortedByDescending { it.quality.toInt() }
                .orEmpty()
    }

    override fun saveCookies(response: Response<MailRuVideosResponse>): Response<MailRuVideosResponse> {
        val cookies = HttpCookie.parse(response.raw().header("Set-Cookie"))
        val videoKeyCookie = cookies.find { it.name == "video_key" }

        if (videoKeyCookie != null) {
            CookieManager.getInstance().setCookie(videoKeyCookie.domain, videoKeyCookie.toString())
        }

        return response
    }
}