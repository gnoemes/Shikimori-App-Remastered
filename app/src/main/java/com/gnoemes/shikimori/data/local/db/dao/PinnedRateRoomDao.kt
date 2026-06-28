package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gnoemes.shikimori.data.local.db.table.PinnedRateTable
import com.gnoemes.shikimori.entity.rates.data.PinnedRateDao
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PinnedRateRoomDao {
    @Query("SELECT * FROM ${PinnedRateTable.TABLE} WHERE ${PinnedRateTable.COLUMN_TYPE} = :type AND ${PinnedRateTable.COLUMN_STATUS} = :status")
    fun getPinnedRates(type: String, status: String): Single<List<PinnedRateDao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rate: PinnedRateDao): Completable

    @Query("DELETE FROM ${PinnedRateTable.TABLE} WHERE ${PinnedRateTable.COLUMN_ID} = :id")
    fun deleteById(id: Long): Completable
}
