package com.gnoemes.shikimori.di.manga

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope
import com.gnoemes.shikimori.di.rate.RateContainerModule
import com.gnoemes.shikimori.di.rate.RateInteractorModule
import com.gnoemes.shikimori.di.rate.SyncModule
import com.gnoemes.shikimori.di.related.RelatedModule
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.di.user.UserUtilModule
import com.gnoemes.shikimori.presentation.presenter.manga.MangaPresenter
import com.gnoemes.shikimori.presentation.view.manga.MangaFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    BaseChildFragmentModule::class,
    MangaRepositoryModule::class,
    MangaUtilModule::class,
    MangaInteractorModule::class,
    RateContainerModule::class,
    UserUtilModule::class,
    UserInteractorModule::class,
    RelatedModule::class,
    RateInteractorModule::class,
    SyncModule::class
])
interface MangaModule {

    @Binds
    fun bindPresenter(presenter: MangaPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    fun bindFragment(fragment: MangaFragment): Fragment
}