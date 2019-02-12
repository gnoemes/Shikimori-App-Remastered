package com.gnoemes.shikimori.entity.manga.data

import com.gnoemes.shikimori.entity.common.data.ImageResponse
import com.gnoemes.shikimori.entity.common.data.LinkedContentResponse
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class MangaResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("russian") val nameRu: String?,
        @field:SerializedName("image") val image: ImageResponse,
        @field:SerializedName("url") val url: String,
        @field:SerializedName("kind") private val _type: MangaType?,
        @field:SerializedName("status") private val _status: Status?,
        @field:SerializedName("volumes") val volumes: Int,
        @field:SerializedName("chapters") val chapters: Int,
        @field:SerializedName("aired_on") val dateAired: DateTime?,
        @field:SerializedName("released_on") val dateReleased: DateTime?
) : LinkedContentResponse() {
    val status: Status
        get() = _status ?: Status.NONE

    val type: MangaType
        get() = _type ?: MangaType.UNKNOWN
}