package com.gnoemes.shikimori.domain.series

import android.util.Log
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.data.repository.series.shikimori.EpisodeChangesRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesSyncRepository
import com.gnoemes.shikimori.data.repository.user.UserRepository
import com.gnoemes.shikimori.domain.rates.RatesInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.entity.series.domain.EpisodeChanges
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SeriesSyncInteractorImpl @Inject constructor(
        private val repository: SeriesSyncRepository,
        private val changesRepository: EpisodeChangesRepository,
        private val userRepository: UserRepository,
        private val ratesInteractor: RatesInteractor,
        private val settingsSource: SettingsSource
) : SeriesSyncInteractor {

    override fun startSync(): Completable = getChanges()
            .switchMapCompletable {
                if (it.isNotEmpty() || (it.firstOrNull()?.rateId == Constants.NO_ID && settingsSource.isAutoStatus))
                    syncEpisodes(it)
                            .onErrorResumeNext { sendChanges(EpisodeChanges.Error(it)) }
                            .andThen(sendChanges(EpisodeChanges.Success))
                else Completable.complete()
            }

    override fun getChanges(): Observable<List<EpisodeChanges.Changes>> =
            changesRepository.getEpisodesChanges()
                    .publish {
                        Log.i("DEVE", "$it")
                        it.buffer(
                                it.debounce(Constants.BIG_DEBOUNCE_INTERVAL, TimeUnit.MILLISECONDS)
                                        .takeUntil(it.ignoreElements().toObservable<List<EpisodeChanges>>())
                        )

                    }
                    .map { list -> list.filter { it is EpisodeChanges.Changes }.map { it as EpisodeChanges.Changes }.distinct() }
                    .doOnNext { Log.i("DEVE", it.toString()) }
                    .applyErrorHandlerAndSchedulers()

    override fun sendChanges(changes: EpisodeChanges): Completable = changesRepository.sendEpisodeChanges(changes)

    override fun syncEpisodes(changes: List<EpisodeChanges.Changes>): Completable =
            (if (changes.size == 1) syncIterable(changes)
            else syncIterable(changes, true).andThen(syncPatch(changes.first().animeId, changes.first().rateId)))
                    .applyErrorHandlerAndSchedulers()


    //uses items to get actual watched episodes (not changes)
    private fun syncPatch(animeId: Long, rateId: Long): Completable =
            (if (rateId == Constants.NO_ID) ratesInteractor.createRateWithResult(animeId, Type.ANIME, RateStatus.WATCHING)
            else Single.just(UserRate(rateId, targetId = animeId, targetType = Type.ANIME)))
                    .flatMap { rate ->
                        repository.getWatchedEpisodesCount(animeId)
                                .map { rate.copy(episodes = it) }
                    }
                    .flatMapCompletable { ratesInteractor.updateRate(it) }

    private fun syncIterable(changes: List<EpisodeChanges.Changes>, onlyLocal: Boolean = false): Completable =
            Observable.fromIterable(changes)
                    .flatMapCompletable { setEpisodeStatus(it.animeId, it.episodeIndex, it.rateId, it.isWatched, onlyLocal) }

    override fun setEpisodeStatus(animeId: Long, episodeId: Int, rateId: Long, isWatching: Boolean, onlyLocal: Boolean): Completable {
        return if (isWatching) setEpisodeWatched(animeId, episodeId, rateId, onlyLocal)
        else setEpisodeUnwatched(animeId, episodeId, rateId, onlyLocal)
    }

    override fun setEpisodeWatched(animeId: Long, episodeId: Int, rateId: Long, onlyLocal: Boolean): Completable =
            repository.isEpisodeWatched(animeId, episodeId)
                    .flatMapCompletable {
                        if (!it) updateRate(animeId, episodeId, rateId, true, onlyLocal)
                        else Completable.complete()
                    }
                    .applyErrorHandlerAndSchedulers()

    override fun setEpisodeUnwatched(animeId: Long, episodeId: Int, rateId: Long, onlyLocal: Boolean): Completable =
            repository.isEpisodeWatched(animeId, episodeId)
                    .flatMapCompletable {
                        if (it) updateRate(animeId, episodeId, rateId, false, onlyLocal)
                        else Completable.complete()
                    }.applyErrorHandlerAndSchedulers()

    private fun updateRate(animeId: Long, episodeId: Int, rateId: Long, isWatched: Boolean, onlyLocal: Boolean): Completable =
            repository.setEpisodeStatus(animeId, episodeId, isWatched)
                    .andThen(
                            if (!onlyLocal) {
                                if (isWatched) incrementOrCreate(animeId, rateId)
                                else decrement(rateId)
                            } else Completable.complete()
                    )

    private fun decrement(rateId: Long): Completable {
        return ratesInteractor.getRate(rateId)
                .flatMapCompletable { ratesInteractor.decrement(it) }
    }

    private fun incrementOrCreate(animeId: Long, rateId: Long): Completable {
        return when (rateId) {
            Constants.NO_ID -> userRepository.getMyUserId()
                    .flatMapCompletable { ratesInteractor.createRate(animeId, Type.ANIME, UserRate(id = rateId, status = RateStatus.WATCHING), it) }
            else -> ratesInteractor.increment(UserRate(rateId, targetType = Type.ANIME, targetId = animeId))
        }
    }
}