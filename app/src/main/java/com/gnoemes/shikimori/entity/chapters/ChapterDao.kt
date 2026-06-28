package com.gnoemes.shikimori.entity.chapters

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.gnoemes.shikimori.data.local.db.table.ChapterTable

@Entity(tableName = ChapterTable.TABLE, primaryKeys = [ChapterTable.COLUMN_MANGA_ID, ChapterTable.COLUMN_CHAPTER_ID])
data class ChapterDao(
        @ColumnInfo(name = ChapterTable.COLUMN_MANGA_ID) val mangaId: Long,
        @ColumnInfo(name = ChapterTable.COLUMN_CHAPTER_ID) val chapterId: Int,
        @ColumnInfo(name = ChapterTable.COLUMN_IS_READED) val isReaded: Int? = null
)