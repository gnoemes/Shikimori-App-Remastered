package com.gnoemes.shikimori.entity.common.presentation

data class DetailsInfoItem(
        val nameSecond: String?,
        val tags: List<DetailsTagItem>,
        val info : List<Any>
)