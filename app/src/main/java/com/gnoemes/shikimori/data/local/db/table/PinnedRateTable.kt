package com.gnoemes.shikimori.data.local.db.table

object PinnedRateTable {

    const val TABLE = "pinned_rates"

    const val COLUMN_ID = "id"

    const val COLUMN_TYPE = "type"

    const val COLUMN_STATUS = "status"

    const val COLUMN_ORDER = "pinned_order"

    const val CREATE_QUERY = "CREATE TABLE $TABLE(" +
            "$COLUMN_ID INTEGER NOT NULL," +
            "$COLUMN_TYPE TEXT NOT NULL," +
            "$COLUMN_STATUS TEXT NOT NULL," +
            "$COLUMN_ORDER INTEGER NOT NULL," +
            "PRIMARY KEY ($COLUMN_ID, $COLUMN_TYPE, $COLUMN_STATUS));"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE"
}
