package com.gnoemes.shikimori.entity.series.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Image
import kotlinx.android.parcel.Parcelize


@Parcelize
data class EpisodesNavigationData(
        private val _animeId: Long,
        private val _image: Image,
        private val _name: String,
        val rateId: Long?
) : BaseSeriesNavigationData(_animeId, _image, _name), Parcelable