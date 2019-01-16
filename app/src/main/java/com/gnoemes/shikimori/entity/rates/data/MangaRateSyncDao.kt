package com.gnoemes.shikimori.entity.rates.data

import com.gnoemes.shikimori.data.local.db.table.MangaRateSyncTable
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType

@StorIOSQLiteType(table = MangaRateSyncTable.TABLE)
data class MangaRateSyncDao @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(name = MangaRateSyncTable.COLUMN_RATE_ID, key = true) val rateId: Long,
        @StorIOSQLiteColumn(name = MangaRateSyncTable.COLUMN_MANGA_ID) val mangaId: Long,
        @StorIOSQLiteColumn(name = MangaRateSyncTable.COLUMN_CHAPTERS) val chapters: Int
)
