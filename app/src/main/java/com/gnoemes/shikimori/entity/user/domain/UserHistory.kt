package com.gnoemes.shikimori.entity.user.domain

import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import org.joda.time.DateTime

data class UserHistory(
        val id: Long,
        val dateCreated: DateTime,
        val description: String,
        val target: LinkedContent?
)
