package com.gnoemes.shikimori.di.player

import androidx.appcompat.app.AppCompatActivity
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseActivityModule
import com.gnoemes.shikimori.di.base.scopes.ActivityScope
import com.gnoemes.shikimori.di.rate.RateInteractorModule
import com.gnoemes.shikimori.di.rate.RateUtilModule
import com.gnoemes.shikimori.di.rate.SyncModule
import com.gnoemes.shikimori.di.series.SeriesInteractorModule
import com.gnoemes.shikimori.di.series.SeriesRepositoryModule
import com.gnoemes.shikimori.di.series.SeriesUtilModule
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.di.user.UserUtilModule
import com.gnoemes.shikimori.presentation.presenter.player.EmbeddedPlayerPresenter
import com.gnoemes.shikimori.presentation.view.player.embedded.EmbeddedPlayerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [
    BaseActivityModule::class,
    RateUtilModule::class,
    RateInteractorModule::class,
    UserUtilModule::class,
    SyncModule::class,
    SeriesRepositoryModule::class,
    SeriesInteractorModule::class,
    SeriesUtilModule::class,
    UserInteractorModule::class
])
interface PlayerModule {

    @Binds
    @ActivityScope
    abstract fun bindPresenter(presenter: EmbeddedPlayerPresenter): MvpPresenter<*>

    @Binds
    @ActivityScope
    fun bindAppCompatActivity(mainActivity: EmbeddedPlayerActivity): AppCompatActivity
}