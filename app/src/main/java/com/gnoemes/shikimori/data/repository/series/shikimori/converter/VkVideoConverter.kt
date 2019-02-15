package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.VkResponse
import com.gnoemes.shikimori.entity.series.domain.Video

interface VkVideoConverter {

    fun convertId(it : Video) : String?

    fun convertTracks(it : Video, vkResponse : VkResponse) : Video
}