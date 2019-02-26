package com.gnoemes.shikimori.entity.app.domain

enum class AnalyticEvent {
    PLAYER_OPENED_WEB,
    PLAYER_OPENED_EMBEDDED,
    PLAYER_OPENED_EXTERNAL;

    override fun toString(): String = super.toString().toLowerCase()
}
