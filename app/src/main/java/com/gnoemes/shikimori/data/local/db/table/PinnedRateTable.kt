package com.gnoemes.shikimori.data.local.db.table

import com.pushtorefresh.storio3.sqlite.queries.Query

object PinnedRateTable {
    const val TABLE = "pinned"

    const val COLUMN_ID = "_id"

    const val COLUMN_TYPE = "type"

    const val COLUMN_STATUS = "status"

    const val COLUMN_ORDER = "pin_order"

    const val CREATE_QUERY = "CREATE TABLE $TABLE(" +
            "$COLUMN_ID INTEGER NOT NULL, " +
            "$COLUMN_TYPE TEXT NOT NULL, " +
            "$COLUMN_STATUS TEXT NOT NULL, " +
            "$COLUMN_ORDER INTEGER, " +
            "PRIMARY KEY ($COLUMN_ID , $COLUMN_TYPE, $COLUMN_STATUS));"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE"

    val ALL_QUERY: Query = Query.builder()
            .table(TABLE)
            .build()
}