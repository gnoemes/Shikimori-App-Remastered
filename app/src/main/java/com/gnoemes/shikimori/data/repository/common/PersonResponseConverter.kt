package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.roles.data.PersonResponse
import com.gnoemes.shikimori.entity.roles.domain.Person
import io.reactivex.functions.Function

interface PersonResponseConverter : Function<List<PersonResponse>, List<Person>> {

    fun convertResponse(it: PersonResponse?): Person?
}