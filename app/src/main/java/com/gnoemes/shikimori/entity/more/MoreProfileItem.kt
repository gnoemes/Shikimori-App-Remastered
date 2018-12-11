package com.gnoemes.shikimori.entity.more

import com.gnoemes.shikimori.entity.user.domain.UserStatus

data class MoreProfileItem(
        val status : UserStatus,
        val name : String?,
        val avatar : String?
)
