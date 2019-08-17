package com.gnoemes.shikimori.entity.similar.domain

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Type
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SimilarNavigationData(
        val id : Long,
        val type : Type
) : Parcelable