package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.PinnedRateDbSource
import com.gnoemes.shikimori.data.local.db.dao.PinnedRateRoomDao
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.data.PinnedRateDao
import com.gnoemes.shikimori.entity.rates.domain.PinnedRate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PinnedRateDbSourceImpl @Inject constructor(
        private val pinnedRateDao: PinnedRateRoomDao
) : PinnedRateDbSource {

    override fun getPinnedRates(type: Type, status: RateStatus): Single<List<PinnedRate>> =
            pinnedRateDao.getPinnedRates(type.name, status.status)
                    .map { daoList -> daoList.map { PinnedRate(it.id, type, status, it.order) } }

    override fun addPinnedRate(rate: PinnedRate): Completable =
            pinnedRateDao.insert(PinnedRateDao(rate.id, rate.type.name, rate.status.status, rate.order))

    override fun removePinnedRate(id: Long): Completable =
            pinnedRateDao.deleteById(id)
}
