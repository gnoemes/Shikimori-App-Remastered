package com.gnoemes.shikimori.entity.series.data

import com.gnoemes.shikimori.data.local.db.table.TranslationSettingTable
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType

@StorIOSQLiteType(table = TranslationSettingTable.TABLE)
data class TranslationSettingDao @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(name = TranslationSettingTable.COLUMN_ANIME_ID, key = true) val animeId: Long,
        @StorIOSQLiteColumn(name = TranslationSettingTable.COLUMN_AUTHOR) val author: String?,
        @StorIOSQLiteColumn(name = TranslationSettingTable.COLUMN_TYPE) val type: String?
)