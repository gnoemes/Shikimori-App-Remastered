package com.gnoemes.shikimori.entity.rates.domain

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Type
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime

@Parcelize
data class UserRate(
        val id: Long? = null,
        val userId: Long? = null,
        val targetId: Long? = null,
        val targetType: Type? = null,
        val score: Double? = null,
        val status: RateStatus? = null,
        val rewatches: Int? = null,
        var episodes: Int? = null,
        val volumes: Int? = null,
        var chapters: Int? = null,
        val text: String? = null,
        val textHtml: String? = null,
        val dateCreated: DateTime? = null,
        val dateUpdated: DateTime? = null
) : Parcelable
