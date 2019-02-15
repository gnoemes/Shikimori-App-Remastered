package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.VkResponse
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import javax.inject.Inject

class VkVideoConverterImpl @Inject constructor() : VkVideoConverter {

    override fun convertId(it: Video): String? {
        val oidRegex = Regex("oid=.+?&")
        val idRegex = Regex("&id=.+?&")

        val url = it.tracks.firstOrNull()?.url ?: ""

        val firstPart = oidRegex.find(url)?.value?.replace("oid=", "")?.replace("&", "")
        val secondPart = idRegex.find(url)?.value?.replace("id=", "")?.replace("&", "")

        return "${firstPart}_$secondPart"
    }

    override fun convertTracks(it: Video, vkResponse: VkResponse): Video {
        val files = vkResponse.response.items.firstOrNull()?.file

        val tracks = mutableListOf<Track>()

        if (files?.src1080 != null) tracks.add(Track("1080", files.src1080))
        if (files?.src720 != null) tracks.add(Track("720", files.src720))
        if (files?.src480 != null) tracks.add(Track("480", files.src480))
        if (files?.src360 != null) tracks.add(Track("360", files.src360))
        if (files?.src240 != null) tracks.add(Track("240", files.src240))

        return Video(it.animeId, it.episodeId, it.hosting, tracks)
    }
}