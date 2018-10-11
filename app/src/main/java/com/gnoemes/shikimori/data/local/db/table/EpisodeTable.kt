package com.gnoemes.shikimori.data.local.db.table

import com.pushtorefresh.storio3.sqlite.queries.Query

object EpisodeTable {

    const val TABLE = "episodes"

    const val COLUMN_ANIME_ID = "_anime_id"

    const val COLUMN_EPISODE_ID = "_episode_id"

    const val COLUMN_IS_WATCHED = "is_watched"

    const val CREATE_QUERY = "CREATE TABLE $TABLE(" +
            "$COLUMN_ANIME_ID INTEGER NOT NULL, " +
            "$COLUMN_EPISODE_ID INTEGER NOT NULL, " +
            "$COLUMN_IS_WATCHED INTEGER, " +
            "PRIMARY KEY ($COLUMN_ANIME_ID , $COLUMN_EPISODE_ID));"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE"

    val ALL_QUERY: Query = Query.builder()
            .table(TABLE)
            .build()
}