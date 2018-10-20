package com.gnoemes.shikimori.data.repository.studio

import com.gnoemes.shikimori.entity.studio.Studio
import com.gnoemes.shikimori.entity.studio.StudioResponse
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class StudioResponseConverterImpl @Inject constructor() : StudioResponseConverter {

    override fun apply(t: List<StudioResponse>): List<Studio> =
            t.map { convertResponse(it) }

    override fun convertResponse(it: StudioResponse): Studio = Studio(
            it.id,
            it.name,
            it.nameFiltered,
            it.isReal,
            it.imageUrl?.appendHostIfNeed()
    )

}