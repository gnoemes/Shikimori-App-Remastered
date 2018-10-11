package com.gnoemes.shikimori.data.repository.club

import com.gnoemes.shikimori.entity.club.data.ClubResponse
import com.gnoemes.shikimori.entity.club.domain.Club
import io.reactivex.functions.Function

interface ClubResponseConverter : Function<List<ClubResponse>, List<Club>> {

    fun convertResponse(it: ClubResponse?): Club?
}