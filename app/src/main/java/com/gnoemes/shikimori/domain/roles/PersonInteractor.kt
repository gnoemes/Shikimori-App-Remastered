package com.gnoemes.shikimori.domain.roles

import com.gnoemes.shikimori.entity.roles.domain.PersonDetails
import io.reactivex.Single

interface PersonInteractor {

    fun getDetails(id: Long): Single<PersonDetails>
}