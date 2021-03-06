package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.FranchiseResponseConverter
import com.gnoemes.shikimori.entity.common.data.FranchiseNodeResponse
import com.gnoemes.shikimori.entity.common.data.FranchiseRelationResponse
import com.gnoemes.shikimori.entity.common.data.FranchiseResponse
import com.gnoemes.shikimori.entity.common.domain.Franchise
import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import com.gnoemes.shikimori.entity.common.domain.FranchiseRelation
import com.gnoemes.shikimori.utils.appendHostIfNeed
import org.joda.time.DateTime
import javax.inject.Inject

class FranchiseResponseConverterImpl @Inject constructor() : FranchiseResponseConverter {
    override fun apply(t: FranchiseResponse): Franchise = Franchise(
                    t.relations.map { convertRelation(it) },
                    t.nodes.map { convertNode(it) }
            )

    private fun convertNode(it: FranchiseNodeResponse): FranchiseNode = FranchiseNode(
            it.id,
            DateTime(it.date),
            it.name,
            it.imageUrl?.appendHostIfNeed(),
            it.url.appendHostIfNeed(),
            it.year,
            it.type,
            it.weight
    )

    private fun convertRelation(it: FranchiseRelationResponse) = FranchiseRelation(
            it.id,
            it.sourceId,
            it.targetId,
            it.sourceNodeIndex,
            it.targetNodeIndex,
            it.weight,
            it.relation
    )
}