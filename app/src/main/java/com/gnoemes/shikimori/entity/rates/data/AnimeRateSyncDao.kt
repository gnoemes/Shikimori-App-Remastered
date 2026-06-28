package com.gnoemes.shikimori.entity.rates.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gnoemes.shikimori.data.local.db.table.AnimeRateSyncTable

@Entity(tableName = AnimeRateSyncTable.TABLE)
data class AnimeRateSyncDao(
        @PrimaryKey
        @ColumnInfo(name = AnimeRateSyncTable.COLUMN_RATE_ID) val rateId: Long,
        @ColumnInfo(name = AnimeRateSyncTable.COLUMN_ANIME_ID) val animeId: Long,
        @ColumnInfo(name = AnimeRateSyncTable.COLUMN_EPISODES) val episodes: Int = 0
)