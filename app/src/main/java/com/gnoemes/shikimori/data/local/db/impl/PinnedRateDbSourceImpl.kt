package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.PinnedRateDbSource
import com.gnoemes.shikimori.data.local.db.table.PinnedRateTable
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.data.PinnedRateDao
import com.gnoemes.shikimori.entity.rates.domain.PinnedRate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PinnedRateDbSourceImpl @Inject constructor(
        private val storio: StorIOSQLite
) : PinnedRateDbSource {

    override fun getPinnedRates(type: Type, status: RateStatus): Single<List<PinnedRate>> =
            storio
                    .get()
                    .listOfObjects(PinnedRateDao::class.java)
                    .withQuery(Query.builder()
                            .table(PinnedRateTable.TABLE)
                            .where("${PinnedRateTable.COLUMN_TYPE} = ? AND ${PinnedRateTable.COLUMN_STATUS} = ?")
                            .whereArgs(type.name, status.status)
                            .build())
                    .prepare()
                    .asRxSingle()
                    .map { daoList -> daoList.map { PinnedRate(it.id, type, status, it.order) } }

    override fun addPinnedRate(rate: PinnedRate): Completable =
            storio
                    .put()
                    .`object`(PinnedRateDao(rate.id, rate.type.name, rate.status.status, rate.order))
                    .prepare()
                    .asRxCompletable()

    override fun removePinnedRate(id: Long): Completable =
            storio
                    .delete()
                    .byQuery(DeleteQuery.builder()
                            .table(PinnedRateTable.TABLE)
                            .where("${PinnedRateTable.COLUMN_ID} = ?")
                            .whereArgs(id)
                            .build())
                    .prepare()
                    .asRxCompletable()
}