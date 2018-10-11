package com.gnoemes.shikimori.data.local.db.table

object MangaRateSyncTable {
    const val TABLE = "rate_sync"

    const val COLUMN_RATE_ID = "_rate_id"

    const val COLUMN_MANGA_ID = "_manga_id"

    const val COLUMN_CHAPTERS = "chapters"

    const val CREATE_QUERY =
            "CREATE TABLE $TABLE (" +
                    "$COLUMN_RATE_ID INTEGER NOT NULL PRIMARY KEY, " +
                    "$COLUMN_MANGA_ID INTEGER NOT NULL, " +
                    "$COLUMN_CHAPTERS INTEGER NOT NULL);"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE"
}