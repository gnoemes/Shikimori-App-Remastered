package com.gnoemes.shikimori.entity.rates.data

import com.gnoemes.shikimori.data.local.db.table.PinnedRateTable
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType

@StorIOSQLiteType(table = PinnedRateTable.TABLE)
data class PinnedRateDao @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(name = PinnedRateTable.COLUMN_ID, key = true) val id: Long,
        @StorIOSQLiteColumn(name = PinnedRateTable.COLUMN_TYPE, key = true) val type: String,
        @StorIOSQLiteColumn(name = PinnedRateTable.COLUMN_STATUS, key = true) val status: String,
        @StorIOSQLiteColumn(name = PinnedRateTable.COLUMN_ORDER) val order: Int
)
