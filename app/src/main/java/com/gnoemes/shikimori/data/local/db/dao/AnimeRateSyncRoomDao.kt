package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gnoemes.shikimori.data.local.db.table.AnimeRateSyncTable
import com.gnoemes.shikimori.entity.rates.data.AnimeRateSyncDao
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface AnimeRateSyncRoomDao {
    @Query("SELECT * FROM ${AnimeRateSyncTable.TABLE} WHERE ${AnimeRateSyncTable.COLUMN_RATE_ID} = :rateId")
    fun getRate(rateId: Long): Maybe<AnimeRateSyncDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rate: AnimeRateSyncDao): Completable

    @Query("SELECT ${AnimeRateSyncTable.COLUMN_EPISODES} FROM ${AnimeRateSyncTable.TABLE} WHERE ${AnimeRateSyncTable.COLUMN_ANIME_ID} = :animeId")
    fun getEpisodeCount(animeId: Long): Maybe<Int>

    @Query("DELETE FROM ${AnimeRateSyncTable.TABLE} WHERE ${AnimeRateSyncTable.COLUMN_ANIME_ID} = :animeId")
    fun deleteByAnimeId(animeId: Long): Completable
}
