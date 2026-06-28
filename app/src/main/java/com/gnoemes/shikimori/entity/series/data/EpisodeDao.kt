package com.gnoemes.shikimori.entity.series.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.gnoemes.shikimori.data.local.db.table.EpisodeTable

@Entity(tableName = EpisodeTable.TABLE, primaryKeys = [EpisodeTable.COLUMN_ANIME_ID, EpisodeTable.COLUMN_EPISODE_ID])
data class EpisodeDao(
        @ColumnInfo(name = EpisodeTable.COLUMN_ANIME_ID) val animeId: Long,
        @ColumnInfo(name = EpisodeTable.COLUMN_EPISODE_ID) val episodeId: Int,
        @ColumnInfo(name = EpisodeTable.COLUMN_IS_WATCHED) val isWatched: Int?
)