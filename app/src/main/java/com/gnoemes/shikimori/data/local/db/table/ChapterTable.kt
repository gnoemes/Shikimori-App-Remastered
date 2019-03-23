package com.gnoemes.shikimori.data.local.db.table

import com.pushtorefresh.storio3.sqlite.queries.Query

object ChapterTable {
    const val TABLE = "chapters"

    const val COLUMN_MANGA_ID = "_manga_id"

    const val COLUMN_CHAPTER_ID = "_chapter_id"

    const val COLUMN_IS_READED = "is_readed"

    const val CREATE_QUERY = "CREATE TABLE $TABLE(" +
            "$COLUMN_MANGA_ID INTEGER NOT NULL, " +
            "$COLUMN_CHAPTER_ID INTEGER NOT NULL, " +
            "$COLUMN_IS_READED INTEGER, " +
            "PRIMARY KEY ($COLUMN_MANGA_ID , $COLUMN_CHAPTER_ID));"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE"

    val ALL_QUERY: Query = Query.builder()
            .table(TABLE)
            .build()
}