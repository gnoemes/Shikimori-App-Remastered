package com.gnoemes.shikimori.entity.chronology

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Type
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChronologyNavigationData(
        val id: Long,
        val type: Type,
        val franchise: String?
) : Parcelable