package com.gnoemes.shikimori.data.repository.roles.converter

import com.gnoemes.shikimori.entity.roles.data.PersonDetailsResponse
import com.gnoemes.shikimori.entity.roles.domain.PersonDetails

interface PersonDetailsConverter {

    fun convertResponse(it: PersonDetailsResponse): PersonDetails
}