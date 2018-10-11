package com.gnoemes.shikimori.entity.app.domain

object Constants {
    const val AUTH_URL = "https://shikimori.org/oauth/authorize?" +
            "client_id=f6f9ff07c7fdca024c5d3395f6dc8d9e802bda458a213d5c382d5d6e69bc77b0&" +
            "redirect_uri=urn%3Aietf%3Awg%3Aoauth%3A2.0%3Aoob&response_type=code"

    const val REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"

    const val DEFAULT_TIMEOUT = 15

    const val DEFAULT_LIMIT = 12

    const val BIG_LIMIT = 30

    const val LONG_TIMEOUT = 30

    const val NO_ID = -1L
}