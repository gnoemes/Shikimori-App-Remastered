package com.gnoemes.shikimori.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gnoemes.shikimori.data.local.db.dao.AnimeRateSyncRoomDao
import com.gnoemes.shikimori.data.local.db.dao.ChapterRoomDao
import com.gnoemes.shikimori.data.local.db.dao.EpisodeRoomDao
import com.gnoemes.shikimori.data.local.db.dao.MangaRateSyncRoomDao
import com.gnoemes.shikimori.data.local.db.dao.PinnedRateRoomDao
import com.gnoemes.shikimori.data.local.db.dao.TranslationSettingRoomDao
import com.gnoemes.shikimori.data.local.db.table.ChapterTable
import com.gnoemes.shikimori.data.local.db.table.PinnedRateTable
import com.gnoemes.shikimori.entity.chapters.ChapterDao
import com.gnoemes.shikimori.entity.rates.data.AnimeRateSyncDao
import com.gnoemes.shikimori.entity.rates.data.MangaRateSyncDao
import com.gnoemes.shikimori.entity.rates.data.PinnedRateDao
import com.gnoemes.shikimori.entity.series.data.EpisodeDao
import com.gnoemes.shikimori.entity.series.data.TranslationSettingDao

@Database(entities = [
    AnimeRateSyncDao::class,
    EpisodeDao::class,
    MangaRateSyncDao::class,
    TranslationSettingDao::class,
    ChapterDao::class,
    PinnedRateDao::class
], version = 3, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animeRateSyncDao(): AnimeRateSyncRoomDao
    abstract fun episodeDao(): EpisodeRoomDao
    abstract fun mangaRateSyncDao(): MangaRateSyncRoomDao
    abstract fun translationSettingDao(): TranslationSettingRoomDao
    abstract fun chapterDao(): ChapterRoomDao
    abstract fun pinnedRateDao(): PinnedRateRoomDao

    companion object {
        @JvmField
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(ChapterTable.CREATE_QUERY)
            }
        }

        @JvmField
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(PinnedRateTable.CREATE_QUERY)
            }
        }
    }
}
