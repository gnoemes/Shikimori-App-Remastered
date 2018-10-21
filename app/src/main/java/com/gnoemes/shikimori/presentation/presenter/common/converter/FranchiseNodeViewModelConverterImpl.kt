package com.gnoemes.shikimori.presentation.presenter.common.converter

import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import javax.inject.Inject

class FranchiseNodeViewModelConverterImpl @Inject constructor() : FranchiseNodeViewModelConverter {

    override fun apply(t: List<FranchiseNode>): List<Pair<String, String>> =
            t.mapIndexed { index, node -> convertNode(index, node) }

    private fun convertNode(index: Int, node: FranchiseNode): Pair<String, String> {
        val hasYear = node.year != null
        val end = if (hasYear) ", ${node.year}" else ""
        val name = "#${index + 1} ${node.name} $end"
        return Pair(name, node.id.toString())
    }
}