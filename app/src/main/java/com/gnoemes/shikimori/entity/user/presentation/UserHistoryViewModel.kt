package com.gnoemes.shikimori.entity.user.presentation

import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import org.joda.time.DateTime

data class UserHistoryViewModel(
        val id : Long,
        val action : String,
        val actionDateString : String,
        val actionDateTime : DateTime,
        val target : LinkedContent?
)