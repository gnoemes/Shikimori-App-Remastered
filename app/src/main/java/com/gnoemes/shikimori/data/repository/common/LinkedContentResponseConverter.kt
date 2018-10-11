package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.common.data.LinkedContentResponse
import com.gnoemes.shikimori.entity.common.domain.LinkedContent

interface LinkedContentResponseConverter {

    fun convertResponse(it: LinkedContentResponse?): LinkedContent?
}