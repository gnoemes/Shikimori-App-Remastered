package com.gnoemes.shikimori.entity.user.domain

import org.joda.time.DateTime

data class UserDetails(
        val id: Long,
        val nickname: String,
        val image: UserImage,
        val dateLastOnline: DateTime,
        val name: String?,
        val sex: String?,
        val website: String?,
        val dateBirth: DateTime?,
        val locale: String?,
        val fullYears: Int?,
        val lastOnline: String,
        val location: String?,
        val isBanned: Boolean,
        val about: String?,
        val commonInfo: String,
        val isShowComments: Boolean,
        val isFriend: Boolean,
        val isIgnored: Boolean,
        val stats: UserStats
)