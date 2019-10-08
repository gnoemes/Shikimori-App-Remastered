package com.gnoemes.shikimori.presentation.presenter.chronology.converter

import com.gnoemes.shikimori.entity.chronology.ChronologyItem
import com.gnoemes.shikimori.entity.chronology.ChronologyViewModel

interface ChronologyViewModelConverter {
    fun apply(t: List<ChronologyItem>, isGuest: Boolean): List<ChronologyViewModel>
}