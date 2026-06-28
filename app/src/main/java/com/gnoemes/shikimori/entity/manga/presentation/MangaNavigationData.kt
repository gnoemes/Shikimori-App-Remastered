package com.gnoemes.shikimori.entity.manga.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Type
import kotlinx.parcelize.Parcelize

@Parcelize
data class MangaNavigationData(
        val id : Long,
        val type : Type
) : Parcelable