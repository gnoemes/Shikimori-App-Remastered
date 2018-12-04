package com.gnoemes.shikimori.presentation.view.common.fragment

import com.gnoemes.shikimori.entity.common.presentation.*
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface BaseDetailsView : BaseFragmentView {

    fun setHeadItem(item : DetailsHeadItem)

    fun setOptionsItem(item : DetailsOptionsItem)

    fun setDescriptionItem(item : DetailsDescriptionItem)

    fun setContentItem(type: DetailsContentType, item : DetailsContentItem)
}