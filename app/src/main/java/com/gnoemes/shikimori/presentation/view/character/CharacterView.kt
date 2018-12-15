package com.gnoemes.shikimori.presentation.view.character

import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadSimpleItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface CharacterView : BaseFragmentView {

    fun setHead(item: DetailsHeadSimpleItem)

    fun setDescription(item: DetailsDescriptionItem)

    fun setContent(type: DetailsContentType, item: DetailsContentItem)

}