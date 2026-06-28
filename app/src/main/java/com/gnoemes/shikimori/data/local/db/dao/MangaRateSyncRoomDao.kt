package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gnoemes.shikimori.data.local.db.table.MangaRateSyncTable
import com.gnoemes.shikimori.entity.rates.data.MangaRateSyncDao
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface MangaRateSyncRoomDao {
    @Query("SELECT * FROM ${MangaRateSyncTable.TABLE} WHERE ${MangaRateSyncTable.COLUMN_RATE_ID} = :rateId")
    fun getRate(rateId: Long): Maybe<MangaRateSyncDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rate: MangaRateSyncDao): Completable

    @Query("SELECT ${MangaRateSyncTable.COLUMN_CHAPTERS} FROM ${MangaRateSyncTable.TABLE} WHERE ${MangaRateSyncTable.COLUMN_MANGA_ID} = :mangaId")
    fun getChaptersCount(mangaId: Long): Maybe<Int>

    @Query("DELETE FROM ${MangaRateSyncTable.TABLE} WHERE ${MangaRateSyncTable.COLUMN_MANGA_ID} = :mangaId")
    fun deleteByMangaId(mangaId: Long): Completable
}
