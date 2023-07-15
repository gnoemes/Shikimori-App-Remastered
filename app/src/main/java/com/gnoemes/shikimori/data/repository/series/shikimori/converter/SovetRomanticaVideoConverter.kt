package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.SovetRomanticaVideosResponse
import com.gnoemes.shikimori.entity.series.domain.Video

interface SovetRomanticaVideoConverter {

    fun convertTracks(it : Video, response : SovetRomanticaVideosResponse) : Video
}