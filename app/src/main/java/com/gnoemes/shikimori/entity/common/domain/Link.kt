package com.gnoemes.shikimori.entity.common.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Link(
        val id: Long,
        val name: String?,
        val url: String
) : Parcelable