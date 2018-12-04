package com.gnoemes.shikimori.presentation.presenter.character.converter

import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import com.gnoemes.shikimori.entity.roles.presentation.CharacterHeadItem
import javax.inject.Inject

class CharacterDetailsViewModelConverterImpl @Inject constructor() : CharacterDetailsViewModelConverter {

    override fun apply(t: CharacterDetails): List<Any> {
        val items = mutableListOf<Any>()

        val head = convertHeadItem(t)
        items.add(head)

        if (!t.description.isNullOrBlank()) {
            items.add(DetailsDescriptionItem(t.description!!))
        }

        if (t.seyu != null && t.seyu.isNotEmpty()) {
//            items.add(DetailsContentItem.Content(DetailsContentType.SEYUS, t.seyu))
        }

        if (t.animes.isNotEmpty()) {
//            items.add(DetailsContentItem.Content(DetailsContentType.ANIMES, t.animes))
        }

        if (t.mangas.isNotEmpty()) {
//            items.add(DetailsContentItem.Content(DetailsContentType.MANGAS, t.mangas))
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