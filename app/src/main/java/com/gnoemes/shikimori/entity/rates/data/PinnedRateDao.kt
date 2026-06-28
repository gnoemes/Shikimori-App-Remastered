package com.gnoemes.shikimori.entity.rates.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.gnoemes.shikimori.data.local.db.table.PinnedRateTable

@Entity(tableName = PinnedRateTable.TABLE, primaryKeys = [PinnedRateTable.COLUMN_ID, PinnedRateTable.COLUMN_TYPE, PinnedRateTable.COLUMN_STATUS])
data class PinnedRateDao(
        @ColumnInfo(name = PinnedRateTable.COLUMN_ID) val id: Long,
        @ColumnInfo(name = PinnedRateTable.COLUMN_TYPE) val type: String,
        @ColumnInfo(name = PinnedRateTable.COLUMN_STATUS) val status: String,
        @ColumnInfo(name = PinnedRateTable.COLUMN_ORDER) val order: Int
)
