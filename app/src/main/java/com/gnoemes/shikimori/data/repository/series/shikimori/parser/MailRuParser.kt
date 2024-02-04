package com.gnoemes.shikimori.data.repository.series.shikimori.parser

import com.gnoemes.shikimori.entity.series.data.MailRuVideosResponse
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import retrofit2.Response

interface MailRuParser {

    fun video(video: TranslationVideo, tracks: List<Track>) : Video

    fun parseVideoMetaUrl(html: String?): String?

    fun tracks(videosMetadata: MailRuVideosResponse?): List<Track>

    fun saveCookies(response: Response<MailRuVideosResponse>): Response<MailRuVideosResponse>
}