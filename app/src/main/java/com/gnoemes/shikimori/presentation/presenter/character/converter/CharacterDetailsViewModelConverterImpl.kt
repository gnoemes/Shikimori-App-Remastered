package com.gnoemes.shikimori.presentation.presenter.character.converter

import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import com.gnoemes.shikimori.entity.roles.presentation.CharacterHeadItem
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import javax.inject.Inject

class CharacterDetailsViewModelConverterImpl @Inject constructor(
        private val contentConverter : DetailsContentViewModelConverter
) : CharacterDetailsViewModelConverter {

    override fun apply(t: CharacterDetails): List<Any> {
        val items = mutableListOf<Any>()

        val head = convertHeadItem(t)
        items.add(head)

        if (!t.description.isNullOrBlank()) {
            items.add(DetailsDescriptionItem(t.description))
        }

        if (t.seyu != null && t.seyu.isNotEmpty()) {
            val item = contentConverter.apply(t.seyu)
            items.add(Pair(DetailsContentType.SEYUS, item))
        }

        if (t.animes.isNotEmpty()) {
            val item = contentConverter.apply(t.animes)
            items.add(Pair(DetailsContentType.ANIMES, item))
        }

        if (t.mangas.isNotEmpty()) {
            val item = contentConverter.apply(t.mangas)
            items.add(Pair(DetailsContentType.MANGAS, item))
        }

        return items
    }

    private fun convertHeadItem(t: CharacterDetails): CharacterHeadItem = CharacterHeadItem(
            t.id,
            t.name,
            t.nameRu,
            t.nameAlt,
            t.image,
            t.url,
            t.nameJp,
            t.description,
            t.descriptionSource
    )
}