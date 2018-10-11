package com.gnoemes.shikimori.data.repository.roles

import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import io.reactivex.Single

interface CharacterRepository {

    fun getDetails(id: Long): Single<CharacterDetails>
}