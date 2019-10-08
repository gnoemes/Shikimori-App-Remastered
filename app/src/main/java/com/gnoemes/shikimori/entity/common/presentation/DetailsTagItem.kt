package com.gnoemes.shikimori.entity.common.presentation

data class DetailsTagItem(
        val id: Long,
        val type: TagType,
        val name: String,
        val raw: Any
) {

    enum class TagType {
        STUDIO, GENRE
    }

}