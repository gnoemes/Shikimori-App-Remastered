package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gnoemes.shikimori.data.local.db.table.EpisodeTable
import com.gnoemes.shikimori.entity.series.data.EpisodeDao
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface EpisodeRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(episodes: List<EpisodeDao>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(episode: EpisodeDao): Completable

    @Query("SELECT * FROM ${EpisodeTable.TABLE} WHERE ${EpisodeTable.COLUMN_ANIME_ID} = :animeId AND ${EpisodeTable.COLUMN_EPISODE_ID} = :episodeId")
    fun getEpisode(animeId: Long, episodeId: Int): Single<EpisodeDao>

    @Query("SELECT COUNT(*) FROM ${EpisodeTable.TABLE} WHERE ${EpisodeTable.COLUMN_ANIME_ID} = :animeId AND ${EpisodeTable.COLUMN_IS_WATCHED} = :isWatched")
    fun getWatchedEpisodesCount(animeId: Long, isWatched: Int): Single<Int>

    @Query("SELECT * FROM ${EpisodeTable.TABLE}")
    fun getAllEpisodes(): Single<List<EpisodeDao>>

    @Query("DELETE FROM ${EpisodeTable.TABLE} WHERE ${EpisodeTable.COLUMN_ANIME_ID} = :animeId")
    fun deleteByAnimeId(animeId: Long): Completable

    @Query("SELECT * FROM ${EpisodeTable.TABLE} WHERE ${EpisodeTable.COLUMN_ANIME_ID} = :animeId AND ${EpisodeTable.COLUMN_IS_WATCHED} = :isWatched")
    fun getEpisodesByWatchStatus(animeId: Long, isWatched: Int): Single<List<EpisodeDao>>
}
