package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.OkVideosResponse
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo

interface OkVideoConverter {

    fun convertTracks(it : TranslationVideo, response : OkVideosResponse) : Video
}