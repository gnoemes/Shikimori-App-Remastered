package com.gnoemes.shikimori.data.local.db.table

object AnimeRateSyncTable {

    const val TABLE = "anime_rate_sync"

    const val COLUMN_RATE_ID = "_rate_id"

    const val COLUMN_ANIME_ID = "_anime_id"

    const val COLUMN_EPISODES = "episodes"

    const val CREATE_QUERY =
            "CREATE TABLE $TABLE (" +
                    "$COLUMN_RATE_ID INTEGER NOT NULL PRIMARY KEY, " +
                    "$COLUMN_ANIME_ID INTEGER NOT NULL, " +
                    "$COLUMN_EPISODES INTEGER NOT NULL);"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE"
}