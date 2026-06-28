package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gnoemes.shikimori.data.local.db.table.TranslationSettingTable
import com.gnoemes.shikimori.entity.series.data.TranslationSettingDao
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface TranslationSettingRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(setting: TranslationSettingDao): Completable

    @Query("SELECT * FROM ${TranslationSettingTable.TABLE} WHERE ${TranslationSettingTable.COLUMN_ANIME_ID} = :animeId")
    fun getSetting(animeId: Long): Maybe<TranslationSettingDao>
}
