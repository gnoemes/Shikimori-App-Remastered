package com.gnoemes.shikimori.entity.user.data

import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.user.domain.MessageType
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime


data class MessageResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("kind") val type: MessageType,
        @field:SerializedName("read") val read: Boolean,
        @field:SerializedName("body") val body: String,
        @field:SerializedName("html_body") val htmlBody: String,
        @field:SerializedName("created_at") var dateCreated: DateTime,
        //TODO Linked content, not only anime (api bug, linked type inside child)
        @field:SerializedName("linked") val linked: AnimeResponse?,
        @field:SerializedName("from") val userFrom: UserBriefResponse,
        @field:SerializedName("to") val userTo: UserBriefResponse
)