package com.gnoemes.shikimori.entity.common.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterItem(
        val action: String,
        val value: String?,
        val localizedText: String?
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other is FilterItem) {
            val (action1, value1, localizedText1) = other
            if (value1 != null && localizedText1 != null && value != null && localizedText != null) {
                return (action == action1
                        && value == value1
                        && localizedText == localizedText1)
            }
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = action.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (localizedText?.hashCode() ?: 0)
        return result
    }
}