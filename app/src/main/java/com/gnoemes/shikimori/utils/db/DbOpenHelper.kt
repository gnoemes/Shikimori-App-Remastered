package com.gnoemes.shikimori.utils.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.gnoemes.shikimori.data.local.db.table.AnimeRateSyncTable
import com.gnoemes.shikimori.data.local.db.table.EpisodeTable
import com.gnoemes.shikimori.data.local.db.table.MangaRateSyncTable
import com.gnoemes.shikimori.data.local.db.table.TranslationSettingTable
import javax.inject.Inject

class DbOpenHelper @Inject constructor(
        context: Context
) : SQLiteOpenHelper(context, DATABASE, null, VERSION) {

    companion object {
        const val DATABASE = "shikimori_database"
        const val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.apply {
            execSQL(AnimeRateSyncTable.CREATE_QUERY)
            execSQL(EpisodeTable.CREATE_QUERY)
            execSQL(MangaRateSyncTable.CREATE_QUERY)
            execSQL(TranslationSettingTable.CREATE_QUERY)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, new: Int) {
    }


}