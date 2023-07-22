package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.VkFileResponse
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo

interface VkVideoConverter {

    fun convertId(it : Video) : String?

    fun convertTracks(it : TranslationVideo, vkResponse : VkFileResponse) : Video

    fun parsePlaylists(html: String?): VkFileResponse
}