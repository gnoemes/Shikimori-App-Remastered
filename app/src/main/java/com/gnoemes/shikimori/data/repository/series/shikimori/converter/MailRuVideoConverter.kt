package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.MailRuVideoResponse
import com.gnoemes.shikimori.entity.series.data.MailRuVideosResponse
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import okhttp3.Response

interface MailRuVideoConverter {

    fun convertTracks(it : TranslationVideo, videos: List<MailRuVideoResponse>) : Video

    fun parseVideoId(embedUrl: String?): String?

    fun parsePlaylists(videosMetadata: MailRuVideosResponse?): List<MailRuVideoResponse>

    fun saveCookies(response: Response)
}