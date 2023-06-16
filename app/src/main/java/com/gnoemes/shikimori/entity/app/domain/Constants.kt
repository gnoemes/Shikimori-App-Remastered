package com.gnoemes.shikimori.entity.app.domain

object Constants {
    const val AUTH_URL = "https://shikimori.one/oauth/authorize?" +
            "client_id=f6f9ff07c7fdca024c5d3395f6dc8d9e802bda458a213d5c382d5d6e69bc77b0&" +
            "redirect_uri=urn%3Aietf%3Awg%3Aoauth%3A2.0%3Aoob&response_type=code"

    const val REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"

    const val DEFAULT_TIMEOUT = 15

    const val DEFAULT_LIMIT = 12

    const val BIG_LIMIT = 30

    const val LONG_TIMEOUT = 30

    const val NO_ID = -1L

    const val MAX_LIMIT = 1000

    const val EXIT_TIMEOUT = 3L * 1000

    const val DEFAULT_DEBOUNCE_INTERVAL = 300L

    const val BIG_DEBOUNCE_INTERVAL = 750L

    const val TASK_LONG_DELAY = 3500L

    const val FOUR_PDA_THEME_URL = "https://4pda.ru/forum/index.php?showtopic=903970"

    const val APP_CLUB_URL = "https://shikimori.one/clubs/1609"

    const val SHIMORI_URL = "https://shimori-us.herokuapp.com/"

    const val SHIKICINEMA_URL = "https://smarthard.net/"

    const val ROAD_MAP_URL = "https://trello.com/b/TeSnqIHY/shikimori-app-public"

    const val DEFAULT_DONATION_LINK = "https://money.yandex.ru/to/410016011857536"

    const val MAX_PINNED_RATES = 3

    const val BACKUP_FILE_NAME= "shimori-backup.json"
}