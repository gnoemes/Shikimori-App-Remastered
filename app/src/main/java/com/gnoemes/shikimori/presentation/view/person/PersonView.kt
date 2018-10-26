package com.gnoemes.shikimori.presentation.view.person

import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface PersonView : BaseFragmentView {

    fun setData(it: List<Any>)
}