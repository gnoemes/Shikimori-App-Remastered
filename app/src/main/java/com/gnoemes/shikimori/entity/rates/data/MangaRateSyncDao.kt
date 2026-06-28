package com.gnoemes.shikimori.entity.rates.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gnoemes.shikimori.data.local.db.table.MangaRateSyncTable

@Entity(tableName = MangaRateSyncTable.TABLE)
data class MangaRateSyncDao(
        @PrimaryKey
        @ColumnInfo(name = MangaRateSyncTable.COLUMN_RATE_ID) val rateId: Long,
        @ColumnInfo(name = MangaRateSyncTable.COLUMN_MANGA_ID) val mangaId: Long,
        @ColumnInfo(name = MangaRateSyncTable.COLUMN_CHAPTERS) val chapters: Int
)