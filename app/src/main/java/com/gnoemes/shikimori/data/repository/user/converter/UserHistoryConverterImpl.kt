package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.data.repository.common.LinkedContentResponseConverter
import com.gnoemes.shikimori.entity.user.data.UserHistoryResponse
import com.gnoemes.shikimori.entity.user.domain.UserHistory
import javax.inject.Inject

class UserHistoryConverterImpl @Inject constructor(
        private val linkedContentConverter: LinkedContentResponseConverter
) : UserHistoryConverter {

    override fun apply(t: List<UserHistoryResponse>): List<UserHistory> =
            t.map { convertResponse(it) }

    private fun convertResponse(it: UserHistoryResponse): UserHistory = UserHistory(
            it.id,
            it.dateCreated,
            it.description,
            linkedContentConverter.convertResponse(it.target)
    )
}