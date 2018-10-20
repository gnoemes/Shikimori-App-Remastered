package com.gnoemes.shikimori.presentation.view.anime

import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface AnimeView : BaseFragmentView {

    fun setAnime(items: List<Any>)

    fun setEpisodes(items: List<Any>)

    fun showEpisodeLoading()

    fun hideEpisodeLoading()

    fun updateSimilar(it: Any)

    fun updateRelated(it: Any)
}