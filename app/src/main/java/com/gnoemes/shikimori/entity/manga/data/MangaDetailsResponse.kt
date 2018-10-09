package com.gnoemes.shikimori.entity.manga.data

import com.gnoemes.shikimori.entity.common.data.GenreResponse
import com.gnoemes.shikimori.entity.common.data.ImageResponse
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.rates.data.UserRateResponse
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class MangaDetailsResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("russian") val nameRu: String?,
        @field:SerializedName("image") val image: ImageResponse,
        @field:SerializedName("url") val url: String,
        @field:SerializedName("kind") val type: MangaType,
        @field:SerializedName("status") private val _status: Status?,
        @field:SerializedName("volumes") val volumes: Int,
        @field:SerializedName("chapters") val chapters: Int,
        @field:SerializedName("aired_on") val dateAired: DateTime?,
        @field:SerializedName("released_on") val dateReleased: DateTime?,
        @field:SerializedName("english") val namesEnglish: List<String?>?,
        @field:SerializedName("japanese") val namesJapanese: List<String?>?,
        @field:SerializedName("score") val score: Double,
        @field:SerializedName("description") val description: String?,
        @field:SerializedName("description_html") val descriptionHtml: String,
        @field:SerializedName("favoured") val favoured: Boolean,
        @field:SerializedName("topic_id") val topicId: Long,
        @field:SerializedName("genres") val genres: List<GenreResponse>,
        @field:SerializedName("user_rate") val userRate: UserRateResponse?
) {

    val status: Status
        get() = _status ?: Status.NONE
}