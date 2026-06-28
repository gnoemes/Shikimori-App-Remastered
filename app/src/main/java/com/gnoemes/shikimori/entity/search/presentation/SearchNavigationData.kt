package com.gnoemes.shikimori.entity.search.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Type
import kotlinx.parcelize.Parcelize

@Parcelize
class SearchNavigationData(
        val payload: SearchPayload?,
        val type: Type
) : Parcelable