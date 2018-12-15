package com.gnoemes.shikimori.presentation.presenter.character.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadSimpleItem
import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import com.gnoemes.shikimori.presentation.presenter.common.converter.BBCodesTextProcessor
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter
import javax.inject.Inject

class CharacterDetailsViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val contentConverter: DetailsContentViewModelConverter,
        private val textProcessor: BBCodesTextProcessor
) : CharacterDetailsViewModelConverter {

    override fun apply(t: CharacterDetails): List<Any> {
        val items = mutableListOf<Any>()

        val head = convertHeadItem(t)
        items.add(head)

        val processedText = t.description?.let { textProcessor.process(it) }
        items.add(DetailsDescriptionItem(processedText))

        items.add(Pair(DetailsContentType.SEYUS, contentConverter.apply(t.seyu)))
        items.add(Pair(DetailsContentType.ANIMES, contentConverter.apply(t.animes)))
        items.add(Pair(DetailsContentType.MANGAS, contentConverter.apply(t.mangas)))

        return items
    }

    private fun convertHeadItem(t: CharacterDetails): DetailsHeadSimpleItem = DetailsHeadSimpleItem(
            Type.CHARACTER,
            t.image,
            context.getString(R.string.on_ru),
            t.nameRu,
            context.getString(R.string.on_jp),
            t.nameJp,
            context.getString(R.string.others),
            t.nameAlt,
            null
    )
}