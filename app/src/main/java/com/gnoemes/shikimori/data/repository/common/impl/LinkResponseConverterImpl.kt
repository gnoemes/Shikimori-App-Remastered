package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.LinkResponseConverter
import com.gnoemes.shikimori.entity.common.data.LinkResponse
import com.gnoemes.shikimori.entity.common.domain.Link
import javax.inject.Inject

class LinkResponseConverterImpl @Inject constructor() : LinkResponseConverter {

    override fun apply(list: List<LinkResponse>): List<Link> = list.map { covertResponse(it) }

    private fun covertResponse(response: LinkResponse): Link = Link(response.id, response.name, response.url)
}