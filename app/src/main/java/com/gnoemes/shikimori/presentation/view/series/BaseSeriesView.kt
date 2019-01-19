package com.gnoemes.shikimori.presentation.view.series

import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface BaseSeriesView : BaseFragmentView {

    fun setBackground(image: Image)

    fun setName(name : String)
}