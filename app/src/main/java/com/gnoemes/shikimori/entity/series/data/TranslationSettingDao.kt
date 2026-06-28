package com.gnoemes.shikimori.entity.series.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gnoemes.shikimori.data.local.db.table.TranslationSettingTable

@Entity(tableName = TranslationSettingTable.TABLE)
data class TranslationSettingDao(
        @PrimaryKey
        @ColumnInfo(name = TranslationSettingTable.COLUMN_ANIME_ID) val animeId: Long,
        @ColumnInfo(name = TranslationSettingTable.COLUMN_AUTHOR) val author: String?,
        @ColumnInfo(name = TranslationSettingTable.COLUMN_TYPE) val type: String?
)