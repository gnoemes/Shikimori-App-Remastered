package com.gnoemes.shikimori.data.local.db.table

object TranslationSettingTable {

    const val TABLE = "translation_settings"

    const val COLUMN_ANIME_ID = "_anime_id"

    const val COLUMN_AUTHOR = "author"

    const val COLUMN_TYPE = "type"

    const val CREATE_QUERY = "CREATE TABLE $TABLE(" +
            "$COLUMN_ANIME_ID INTEGER NOT NULL PRIMARY KEY, " +
            "$COLUMN_AUTHOR TEXT, " +
            "$COLUMN_TYPE TEXT);"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE"
}
