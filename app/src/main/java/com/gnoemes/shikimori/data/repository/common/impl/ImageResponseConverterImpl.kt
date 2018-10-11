package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.entity.common.data.ImageResponse
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class ImageResponseConverterImpl @Inject constructor() : ImageResponseConverter {

    override fun convertResponse(response: ImageResponse): Image = Image(
            response.original?.appendHostIfNeed(),
            response.preview?.appendHostIfNeed(),
            response.x96?.appendHostIfNeed(),
            response.x48?.appendHostIfNeed()
    )
}