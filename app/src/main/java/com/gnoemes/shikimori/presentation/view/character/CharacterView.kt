package com.gnoemes.shikimori.presentation.view.character

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadSimpleItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

@StateStrategyType(AddToEndSingleStrategy::class)
interface CharacterView : BaseFragmentView {

    fun setHead(item: DetailsHeadSimpleItem)

    fun setDescription(item: DetailsDescriptionItem)

    fun setSeyuContent(item: DetailsContentItem)
    fun setAnimeContent(item: DetailsContentItem)
    fun setMangaContent(item: DetailsContentItem)

}