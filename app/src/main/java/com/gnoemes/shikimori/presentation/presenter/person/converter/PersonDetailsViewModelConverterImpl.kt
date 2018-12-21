package com.gnoemes.shikimori.presentation.presenter.person.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadSimpleItem
import com.gnoemes.shikimori.entity.roles.domain.PersonDetails
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import org.joda.time.DateTime
import org.joda.time.Years
import javax.inject.Inject

class PersonDetailsViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val dateTimeConverter: DateTimeConverter,
        private val contentConverter: DetailsContentViewModelConverter
) : PersonDetailsViewModelConverter {

    override fun apply(t: PersonDetails): List<Any> {
        val items = mutableListOf<Any>()

        val head = convertHeadItem(t)
        items.add(head)

        val roles = convertRoles(t.roles)
        items.add(DetailsDescriptionItem(roles))

        items.add(Pair(DetailsContentType.CHARACTERS, contentConverter.apply(t.characters)))
        items.add(Pair(DetailsContentType.WORKS, contentConverter.apply(t.works)))

        return items
    }

    private fun convertRoles(roles: List<List<String?>?>): String? {
        if (roles.isEmpty()) {
            return null
        }

        val builder = StringBuilder()
        roles.forEach { role ->
            var first = true
            role?.forEach {
                if (!it.isNullOrBlank()) {
                    builder.append(it)
                    if (first) builder.append(": ")
                    else builder.append("<br>")
                    first = false
                }
            }
        }

        return builder.toString()
    }

    private fun convertHeadItem(t: PersonDetails): DetailsHeadSimpleItem = DetailsHeadSimpleItem(
            Type.PERSON,
            t.image,
            context.getString(R.string.on_eng),
            t.name,
            context.getString(R.string.on_jp),
            t.nameJp,
            context.getString(R.string.person_birthday),
            convertBirthDay(t.birthDay),
            t.jobTitle
    )

    private fun convertBirthDay(birthDay: DateTime?): String? {
        val date = dateTimeConverter.convertToFullHumanDateString(birthDay)
        return if (date != null) {
            val years = Years.yearsBetween(birthDay, DateTime.now()).years
            "$date ($years)"
        } else null
    }
}