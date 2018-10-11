package com.gnoemes.shikimori.entity.chapters

import com.gnoemes.shikimori.data.local.db.table.ChapterTable
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType

@StorIOSQLiteType(table = ChapterTable.TABLE)
data class ChapterDao @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(name = ChapterTable.COLUMN_MANGA_ID, key = true) val mangaId: Long,
        @StorIOSQLiteColumn(name = ChapterTable.COLUMN_CHAPTER_ID, key = true) val chapterId: Int,
        @StorIOSQLiteColumn(name = ChapterTable.COLUMN_IS_READED) val isReaded: Int? = null
)