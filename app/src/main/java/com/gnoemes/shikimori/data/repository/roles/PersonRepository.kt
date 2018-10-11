package com.gnoemes.shikimori.data.repository.roles

import com.gnoemes.shikimori.entity.roles.domain.PersonDetails
import io.reactivex.Single

interface PersonRepository {

    fun getDetails(id: Long): Single<PersonDetails>
}