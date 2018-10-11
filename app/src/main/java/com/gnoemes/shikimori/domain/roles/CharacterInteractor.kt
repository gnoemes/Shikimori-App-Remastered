package com.gnoemes.shikimori.domain.roles

import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import io.reactivex.Single

interface CharacterInteractor {

    fun getDetails(id: Long): Single<CharacterDetails>
}