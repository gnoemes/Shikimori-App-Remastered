package com.gnoemes.shikimori.data.repository.studio

import com.gnoemes.shikimori.entity.studio.Studio
import com.gnoemes.shikimori.entity.studio.StudioResponse
import io.reactivex.functions.Function

interface StudioResponseConverter : Function<List<StudioResponse>, List<Studio>> {

    fun convertResponse(it: StudioResponse): Studio
}