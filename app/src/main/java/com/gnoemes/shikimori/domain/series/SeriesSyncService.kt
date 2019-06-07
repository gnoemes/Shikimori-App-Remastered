package com.gnoemes.shikimori.domain.series

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.series.domain.EpisodeChanges
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SeriesSyncService : Service() {

    @Inject
    lateinit var interactor: SeriesSyncInteractor

    @Inject
    lateinit var settingsSource: SettingsSource

    private val binder by lazy { LocalBinder() }

    private var disposable: CompositeDisposable? = null

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()

        val d =
                interactor.getChanges()
                        .subscribe({ syncEpisodes(it) }, {})
        disposable?.add(d)
    }

    private fun syncEpisodes(changes: List<EpisodeChanges.Changes>) {
        if (changes.isNotEmpty() || (changes.firstOrNull()?.rateId == Constants.NO_ID && settingsSource.isAutoStatus)) {
            val d =
                    interactor.syncEpisodes(changes)
                            .onErrorResumeNext { interactor.sendChanges(EpisodeChanges.Error(it)) }
                            .andThen(interactor.sendChanges(EpisodeChanges.Success))
                            .subscribe({}, {})
            disposable?.add(d)
        }

    }

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onDestroy() {
        super.onDestroy()
        if (disposable?.isDisposed == false) disposable?.dispose()
    }

    inner class LocalBinder : Binder() {
        fun getService(): SeriesSyncService = this@SeriesSyncService
    }
}