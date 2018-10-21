package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.FranchiseResponseConverter
import com.gnoemes.shikimori.entity.common.data.FranchiseNodeResponse
import com.gnoemes.shikimori.entity.common.data.FranchiseResponse
import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import com.gnoemes.shikimori.utils.appendHostIfNeed
import org.joda.time.DateTime
import javax.inject.Inject

class FranchiseResponseConverterImpl @Inject constructor() : FranchiseResponseConverter {
    override fun apply(list: FranchiseResponse): List<FranchiseNode> =
            list.nodes.map { convertResponse(it) }

    private fun convertResponse(it: FranchiseNodeResponse): FranchiseNode = FranchiseNode(
            it.id,
            DateTime(it.date),
            it.name,
            it.imageUrl?.appendHostIfNeed(),
            it.url.appendHostIfNeed(),
            it.year,
            it.type,
            it.weight
    )
}