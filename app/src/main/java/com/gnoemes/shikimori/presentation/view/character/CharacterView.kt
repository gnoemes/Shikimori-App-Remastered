package com.gnoemes.shikimori.presentation.view.character

import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface CharacterView : BaseFragmentView {

    fun setData(it: List<Any>)
}