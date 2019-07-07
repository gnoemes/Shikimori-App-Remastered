package com.gnoemes.shikimori.domain.series

import android.util.Log
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
        private val interactor: SeriesInteractor,
        private val ratesInteractor: RatesInteractor
) : SeriesSyncInteractor {

    override fun getChanges(): Observable<List<EpisodeChanges.Changes>> =
            interactor.getEpisodeChanges()
                    .publish {
                        Log.i("DEVE","$it")
                        it.buffer(
                                it.debounce(Constants.BIG_DEBOUNCE_INTERVAL, TimeUnit.MILLISECONDS)
                                        .takeUntil(it.ignoreElements().toObservable<List<EpisodeChanges>>())
                        )

                    }
                    .map { list -> list.filter { it is EpisodeChanges.Changes }.map { it as EpisodeChanges.Changes }.distinct() }
                    .doOnNext { Log.i("DEVE", it.toString()) }
                    .applyErrorHandlerAndSchedulers()

    override fun sendChanges(changes: EpisodeChanges): Completable = interactor.sendEpisodeChanges(changes)

    override fun syncEpisodes(changes: List<EpisodeChanges.Changes>): Completable =
            (if (changes.size == 1) syncIterable(changes)
            else syncIterable(changes, true).andThen(syncPatch(changes.first().animeId, changes.first().rateId)))
                    .applyErrorHandlerAndSchedulers()


    //uses items to get actual watched episodes (not changes)
    private fun syncPatch(animeId: Long, rateId: Long): Completable =
            (if (rateId == Constants.NO_ID) ratesInteractor.createRateWithResult(animeId, Type.ANIME, RateStatus.WATCHING)
            else Single.just(UserRate(rateId, targetId = animeId, targetType = Type.ANIME)))
                    .flatMap { rate ->
                        interactor.getWatchedEpisodesCount(animeId)
                                .map { rate.copy(episodes = it) }
                    }
                    .flatMapCompletable { ratesInteractor.updateRate(it) }

    private fun syncIterable(changes: List<EpisodeChanges.Changes>, onlyLocal: Boolean = false): Completable =
            Observable.fromIterable(changes)
                    .flatMapCompletable { interactor.setEpisodeStatus(it.animeId, it.episodeIndex, it.rateId, it.isWatched, onlyLocal) }
}