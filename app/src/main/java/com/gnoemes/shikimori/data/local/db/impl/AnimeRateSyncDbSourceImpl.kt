package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.dao.AnimeRateSyncRoomDao
import com.gnoemes.shikimori.entity.rates.data.AnimeRateSyncDao
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AnimeRateSyncDbSourceImpl @Inject constructor(
        private val animeRateSyncDao: AnimeRateSyncRoomDao
) : AnimeRateSyncDbSource {

    override fun getRate(rateId: Long): Single<UserRate> =
            animeRateSyncDao.getRate(rateId)
                    .map { UserRate(it.rateId, targetId = it.animeId, episodes = it.episodes) }
                    .toSingle()


    override fun saveRate(userRate: UserRate): Completable {
        val rateId = userRate.id ?: return Completable.complete()
        val targetId = userRate.targetId ?: return Completable.complete()
        val episodes = userRate.episodes ?: return Completable.complete()
        return animeRateSyncDao.insert(AnimeRateSyncDao(rateId, targetId, episodes))
                .onErrorResumeNext { it.printStackTrace(); Completable.complete() }
    }


    override fun getEpisodeCount(animeId: Long): Single<Int> =
            animeRateSyncDao.getEpisodeCount(animeId)
                    .toSingle(0)

    override fun clearRate(animeId: Long): Completable =
            animeRateSyncDao.deleteByAnimeId(animeId)
}
