package com.gnoemes.shikimori.presentation.presenter.common.converter

import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.utils.firstUpperCase
import javax.inject.Inject

class LinkViewModelConverterImpl @Inject constructor() : LinkViewModelConverter {

    override fun apply(t: List<Link>): List<Pair<String, String>> =
            t.asSequence().filter { !it.name.isNullOrBlank() }.map { convertLink(it) }.toList()

    private fun convertLink(it: Link): Pair<String, String> =
            Pair(it.name!!.replace("_", " ").firstUpperCase()!!, it.url)

}