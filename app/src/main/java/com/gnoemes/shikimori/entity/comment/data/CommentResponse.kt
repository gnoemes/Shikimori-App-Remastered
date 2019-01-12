package com.gnoemes.shikimori.entity.comment.data

import com.gnoemes.shikimori.entity.comment.domain.CommentableType
import com.gnoemes.shikimori.entity.user.data.UserBriefResponse
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class CommentResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("user_id") val userId: Long,
        @field:SerializedName("commentable_id") val commentableId: Long,
        @field:SerializedName("commentable_type") val commentableType: CommentableType,
        @field:SerializedName("body") val body: String?,
        @field:SerializedName("html_body") val bodyHtml: String?,
        @field:SerializedName("created_at") val dateCreated: DateTime,
        @field:SerializedName("updated_at") val dateUpdated: DateTime,
        @field:SerializedName("is_offtopic") val isOfftopic: Boolean,
        @field:SerializedName("is_summary") val isSummary: Boolean,
        @field:SerializedName("can_be_edited") val isEditable: Boolean,
        @field:SerializedName("user") val user: UserBriefResponse
)