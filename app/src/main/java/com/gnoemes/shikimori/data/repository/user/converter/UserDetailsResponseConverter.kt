package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.entity.user.data.UserDetailsResponse
import com.gnoemes.shikimori.entity.user.domain.UserDetails

interface UserDetailsResponseConverter {

    fun convertResponse(it: UserDetailsResponse): UserDetails
}