package com.gnoemes.shikimori.entity.common.domain

import ru.terrakok.cicerone.Screen

/**
 * A Screen implementation that wraps a string key and optional transition data.
 * Compatible with Cicerone 5.x API while preserving the existing string-key-based routing.
 */
class KeyScreen(key: String, val transitionData: Any? = null) : Screen() {
    init {
        screenKey = key
    }
}
