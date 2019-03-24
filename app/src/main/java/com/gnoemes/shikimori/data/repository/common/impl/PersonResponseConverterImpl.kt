package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.data.repository.common.PersonResponseConverter
import com.gnoemes.shikimori.entity.roles.data.PersonResponse
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class PersonResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter
) : PersonResponseConverter {

    override fun apply(t: List<PersonResponse>): List<Person> = t.mapNotNull { convertResponse(it) }

    override fun convertResponse(it: PersonResponse?): Person? {
        if (it == null) {
            return null
        }

        return Person(it.id,
                it.name.trim(),
                it.nameRu?.trim(),
                imageConverter.convertResponse(it.image),
                it.url.appendHostIfNeed()
        )
    }
}