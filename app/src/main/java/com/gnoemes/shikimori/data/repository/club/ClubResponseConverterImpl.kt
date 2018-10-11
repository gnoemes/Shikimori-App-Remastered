package com.gnoemes.shikimori.data.repository.club

import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.entity.club.data.ClubResponse
import com.gnoemes.shikimori.entity.club.domain.Club
import javax.inject.Inject

class ClubResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter
) : ClubResponseConverter {

    override fun apply(t: List<ClubResponse>): List<Club> = t.map { convertResponse(it)!! }

    override fun convertResponse(it: ClubResponse?): Club? {
        if (it == null) {
            return null
        }


        return Club(
                it.id,
                it.name,
                imageConverter.convertResponse(it.image),
                it.isCensored,
                it.policyJoin,
                it.policyComment
        )
    }
}