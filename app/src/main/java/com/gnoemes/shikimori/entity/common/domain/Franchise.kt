package com.gnoemes.shikimori.entity.common.domain

data class Franchise(
        val relations : List<FranchiseRelation>,
        val nodes : List<FranchiseNode>
)