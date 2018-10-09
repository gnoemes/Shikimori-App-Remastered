package com.gnoemes.shikimori.entity.roles.data

import com.gnoemes.shikimori.entity.common.data.ImageResponse
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class PersonDetailsResponse(
        @field:SerializedName("id") val id : Long,
        @field:SerializedName("name") val name : String,
        @field:SerializedName("russian") val nameRu : String?,
        @field:SerializedName("japanese") val nameJp : String?,
        @field:SerializedName("image") val image : ImageResponse,
        @field:SerializedName("url") val url : String,
        @field:SerializedName("job_title") val jobTitle : String?,
        @field:SerializedName("birthday") val birthDay : DateTime?,
        @field:SerializedName("works") val works : List<WorkResponse>?,
        @field:SerializedName("roles") val roles : List<SeyuRoleResponse>?,
        @field:SerializedName("groupped_roles") val rolesGrouped : List<List<String?>?>,
        @field:SerializedName("topic_id") val topicId : Long,
        @field:SerializedName("person_favoured") val isFavoritePerson : Boolean,
        @field:SerializedName("producer") val isProducer : Boolean,
        @field:SerializedName("producer_favoured") val isFavoriteProducer : Boolean,
        @field:SerializedName("mangaka") val isMangaka : Boolean,
        @field:SerializedName("mangaka_favoured") val isFavoriteMangaka : Boolean,
        @field:SerializedName("seyu") val isSeyu : Boolean,
        @field:SerializedName("seyu_favoured") val isFavoriteSeyu : Boolean
)