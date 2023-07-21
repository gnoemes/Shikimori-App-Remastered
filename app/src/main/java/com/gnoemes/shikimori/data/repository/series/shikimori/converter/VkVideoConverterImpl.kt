package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.VkFileResponse
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import javax.inject.Inject

class VkVideoConverterImpl @Inject constructor() : VkVideoConverter {

    override fun convertId(it: Video): String? {
        val oidRegex = Regex("oid=.+?(\\D|$)")
        val idRegex = Regex("\\Wid=.+?(&|\\D|$)")

        val url = it.tracks.firstOrNull()?.url ?: ""

        val firstPart = oidRegex.find(url)?.value?.replace("oid=", "")?.replace("&", "")
        val secondPart = idRegex.find(url)?.value?.replace("id=", "")?.replace("&", "")

        return "${firstPart}_$secondPart"
    }

    override fun convertTracks(it: TranslationVideo, vkResponse: VkFileResponse): Video {
        val tracks = mutableListOf<Track>()

        if (vkResponse.src1080 != null) tracks.add(Track("1080", vkResponse.src1080!!))
        if (vkResponse.src720 != null) tracks.add(Track("720", vkResponse.src720!!))
        if (vkResponse.src480 != null) tracks.add(Track("480", vkResponse.src480!!))
        if (vkResponse.src360 != null) tracks.add(Track("360", vkResponse.src360!!))
        if (vkResponse.src240 != null) tracks.add(Track("240", vkResponse.src240!!))

        return Video(it.animeId, it.episodeIndex.toLong(), it.webPlayerUrl!!, it.videoHosting, tracks, null, null)
    }

    override fun parsePlaylists(html: String): VkFileResponse {
        val regex = Regex("\"(url240|url360|url480|url720|url1080)\":\"(.*?)\"")
        val matches = regex.findAll(html)
        val vkResponse = VkFileResponse(null, null, null, null, null)

        matches.map { it.destructured.toList() }.forEach {
            val (key, value) = it
            when (key) {
                "url240" -> vkResponse.src240 = value
                "url360" -> vkResponse.src360 = value
                "url480" -> vkResponse.src480 = value
                "url720" -> vkResponse.src720= value
                "url1080" -> vkResponse.src1080 = value
            }
        }

        return vkResponse
    }
}