package com.gnoemes.shikimori.data.local.db.table

import com.pushtorefresh.storio3.sqlite.queries.Query

object TranslationSettingTable {

    const val TABLE = "translation_setting"

    const val COLUMN_ANIME_ID = "_anime_id"

    const val COLUMN_EPISODE_INDEX = "_episode_index"

    const val COLUMN_AUTHOR = "author"

    const val COLUMN_TYPE = "type"

    const val CREATE_QUERY = "CREATE TABLE $TABLE(" +
            "$COLUMN_ANIME_ID INTEGER NOT NULL, " +
            "$COLUMN_EPISODE_INDEX INTEGER NOT NULL, " +
            "$COLUMN_AUTHOR TEXT, " +
            "$COLUMN_TYPE TEXT, " +
            "PRIMARY KEY ($COLUMN_ANIME_ID , $COLUMN_EPISODE_INDEX));"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE"

    val ALL_QUERY: Query = Query.builder()
            .table(TABLE)
            .build()
}