package com.gnoemes.shikimori.entity.user.domain

import org.joda.time.DateTime

data class UserBrief(
        val id: Long,
        val nickname: String,
        val avatar: String?,
        val image: UserImage,
        val dateLastOnline: DateTime,
        val name: String?,
        val sex: String?,
        val website: String?,
        val dateBirth: DateTime?,
        val locale: String?
)