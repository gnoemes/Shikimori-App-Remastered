package com.gnoemes.shikimori.entity.series.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Image
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TranslationsNavigationData(
        private val _animeId: Long,
        private val _image: Image,
        private val _name: String,
        val episodeId: Long,
        val episodeIndex: Int,
        val rateId: Long,
        val isAlternative: Boolean
) : BaseSeriesNavigationData(_animeId, _image, _name), Parcelable