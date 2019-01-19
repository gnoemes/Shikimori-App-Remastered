package com.gnoemes.shikimori.domain.rates

import com.gnoemes.shikimori.data.repository.rates.RatesRepository
import com.gnoemes.shikimori.data.repository.user.UserRepository
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RatesInteractorImpl @Inject constructor(
        private val repository: RatesRepository,
        private val userRepository: UserRepository
) : RatesInteractor {

    override fun getAnimeRates(id: Long, page: Int, limit: Int, rateStatus: RateStatus): Single<List<Rate>> =
            repository.getAnimeRates(id, page, limit, rateStatus)
                    .applyErrorHandlerAndSchedulers()

    override fun getMangaRates(id: Long, page: Int, limit: Int, rateStatus: RateStatus): Single<List<Rate>> =
            repository.getMangaRates(id, page, limit, rateStatus)
                    .applyErrorHandlerAndSchedulers()

    override fun getRate(id: Long): Single<UserRate> = repository.getRate(id).applyErrorHandlerAndSchedulers()

    override fun syncRate(id: Long): Completable =
            userRepository.getMyUserBrief()
                    .flatMapCompletable { user ->
                        repository.getRate(id)
                                .filter { user.id == it.userId }
                                .flatMapCompletable { repository.syncRate(it) }
                    }.applyErrorHandlerAndSchedulers()

    override fun deleteRate(id: Long): Completable = repository.deleteRate(id).applyErrorHandlerAndSchedulers()

    override fun createRate(id: Long, type: Type, rate: UserRate, userId: Long): Completable =
            repository.createRate(id, type, rate, userId)
                    .applyErrorHandlerAndSchedulers()

    override fun createRateWithResult(id: Long, type: Type, status: RateStatus): Single<UserRate> =
            userRepository.getMyUserBrief()
                    .flatMap { repository.createRateWithResult(id, type, UserRate(status = status), it.id) }
                    .applyErrorHandlerAndSchedulers()

    override fun updateRate(rate: UserRate): Completable =
            repository.updateRate(rate)
                    .applyErrorHandlerAndSchedulers()

    override fun changeRateStatus(rateId: Long, newStatus: RateStatus): Completable =
            repository.getRate(rateId)
                    .flatMapCompletable { repository.updateRate(it.copy(status = newStatus)) }
                    .applyErrorHandlerAndSchedulers()

    override fun increment(rateId: Long): Completable =
            repository.increment(rateId)
                    .applyErrorHandlerAndSchedulers()

    override fun decrement(rate: UserRate): Completable =
            Single.fromCallable { rate }
                    .map { if (it.targetType == Type.ANIME) it.copy(episodes = it.episodes?.minus(1)) else it.copy(chapters = it.chapters?.minus(1)) }
                    .flatMapCompletable { repository.updateRate(it) }
                    .applyErrorHandlerAndSchedulers()
}