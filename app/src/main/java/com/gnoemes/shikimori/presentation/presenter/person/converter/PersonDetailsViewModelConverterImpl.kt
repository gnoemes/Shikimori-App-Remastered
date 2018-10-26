package com.gnoemes.shikimori.presentation.presenter.person.converter

import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.roles.domain.PersonDetails
import com.gnoemes.shikimori.entity.roles.presentation.PersonDescriptionItem
import com.gnoemes.shikimori.entity.roles.presentation.PersonHeadItem
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import javax.inject.Inject

class PersonDetailsViewModelConverterImpl @Inject constructor(
        private val dateTimeConverter: DateTimeConverter
) : PersonDetailsViewModelConverter {

    override fun apply(t: PersonDetails): List<Any> {
        val items = mutableListOf<Any>()

        val head = convertHeadItem(t)
        items.add(head)

        if (t.roles.isNotEmpty()) {
            val roles = convertRoles(t.roles)
            items.add(roles)
        }

        if (t.characters != null && t.characters.isNotEmpty()) {
            items.add(DetailsContentItem.Content(DetailsContentType.ROLES, t.characters))
        }

        if (t.works != null && t.works.isNotEmpty()) {
            items.add(DetailsContentItem.Content(DetailsContentType.WORKS, t.works))
        }

        return items
    }

    private fun convertRoles(roles: List<List<String?>?>): PersonDescriptionItem {
        val builder = StringBuilder()

        roles.forEach { role ->
            var first = true
            role?.forEach {
                builder.append(it)
                if (first) builder.append(": ")
                else builder.append("\n")
                first = false
            }
        }

        return PersonDescriptionItem(builder.toString())
    }

    private fun convertHeadItem(t: PersonDetails): PersonHeadItem = PersonHeadItem(
            t.name,
            t.nameRu,
            t.nameJp,
            t.image,
            t.jobTitle,
            dateTimeConverter.convertToFullHumanDateString(t.birthDay)
    )
}