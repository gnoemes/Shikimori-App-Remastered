package com.gnoemes.shikimori.entity.rates.data

import com.gnoemes.shikimori.data.local.db.table.AnimeRateSyncTable
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType

@StorIOSQLiteType(table = AnimeRateSyncTable.TABLE)
data class AnimeRateSyncDao @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(name = AnimeRateSyncTable.COLUMN_RATE_ID, key = true) val rateId: Long,
        @StorIOSQLiteColumn(name = AnimeRateSyncTable.COLUMN_ANIME_ID) val animeId: Long,
        @StorIOSQLiteColumn(name = AnimeRateSyncTable.COLUMN_EPISODES) val episodes: Int
)