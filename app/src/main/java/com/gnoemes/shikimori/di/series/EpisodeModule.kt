package com.gnoemes.shikimori.di.series

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomScope
import com.gnoemes.shikimori.di.rate.RateInteractorModule
import com.gnoemes.shikimori.di.rate.RateUtilModule
import com.gnoemes.shikimori.di.rate.SyncModule
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.di.user.UserUtilModule
import com.gnoemes.shikimori.presentation.presenter.series.episodes.EpisodesPresenter
import com.gnoemes.shikimori.presentation.view.series.episodes.EpisodesFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    RateUtilModule::class,
    RateInteractorModule::class,
    SyncModule::class,
    UserUtilModule::class,
    EpisodeRepositoryModule::class,
    EpisodeInteractorModule::class,
    EpisodeUtilModule::class,
    UserInteractorModule::class
])
interface EpisodeModule {

    @Binds
    fun bindPresenter(presenter: EpisodesPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomScope
    fun bindFragment(fragment: EpisodesFragment): Fragment
}