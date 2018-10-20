package com.gnoemes.shikimori.entity.common.presentation

sealed class DetailsContentItem(val type: DetailsContentType) {
    data class Content(val contentType: DetailsContentType, val items: List<Any>) : DetailsContentItem(contentType)
    data class Loading(val contentType: DetailsContentType) : DetailsContentItem(contentType)
    data class Empty(val contentType: DetailsContentType) : DetailsContentItem(contentType)
}