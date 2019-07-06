package com.gnoemes.shikimori.utils.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.gnoemes.shikimori.data.local.db.table.*
import javax.inject.Inject

class DbOpenHelper @Inject constructor(
        context: Context
) : SQLiteOpenHelper(context, DATABASE, null, VERSION) {

    companion object {
        const val DATABASE = "shikimori_database"
        const val VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.apply {
            execSQL(AnimeRateSyncTable.CREATE_QUERY)
            execSQL(EpisodeTable.CREATE_QUERY)
            execSQL(MangaRateSyncTable.CREATE_QUERY)
            execSQL(TranslationSettingTable.CREATE_QUERY)
            execSQL(ChapterTable.CREATE_QUERY)
            execSQL(PinnedRateTable.CREATE_QUERY)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, new: Int) {
        if (old < 2) {
            db?.execSQL(ChapterTable.CREATE_QUERY)
        }

        if (old < 3) {
            db?.execSQL(PinnedRateTable.CREATE_QUERY)
        }
    }


}