package com.gnoemes.shikimori.data.repository.series.shikimori

import com.gnoemes.shikimori.entity.series.domain.EpisodeChanges
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class EpisodeChangesRepositoryImpl @Inject constructor(
) : EpisodeChangesRepository {

    private val changeSource = PublishSubject.create<EpisodeChanges>()

    override fun getEpisodesChanges(): Observable<EpisodeChanges> = changeSource

    override fun sendEpisodeChanges(changes: EpisodeChanges): Completable = Completable.fromAction { changeSource.onNext(changes) }
}