package com.gnoemes.shikimori.entity.common.domain

data class FranchiseRelation(
        val id: Long,
        val sourceId: Long,
        val targetId: Long,
        val sourceNodeIndex: Int,
        val targetNodeIndex: Int,
        val weight: Int,
        val relation: RelationType
)