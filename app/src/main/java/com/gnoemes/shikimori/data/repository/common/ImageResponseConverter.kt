package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.common.data.ImageResponse
import com.gnoemes.shikimori.entity.common.domain.Image

interface ImageResponseConverter {

    fun convertResponse(response: ImageResponse): Image
}