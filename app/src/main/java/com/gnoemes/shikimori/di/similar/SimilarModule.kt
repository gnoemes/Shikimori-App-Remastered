package com.gnoemes.shikimori.di.similar

import androidx.fragment.app.Fragment
import com.gnoemes.shikimori.di.anime.AnimeRepositoryModule
import com.gnoemes.shikimori.di.anime.AnimeUtilModule
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope
import com.gnoemes.shikimori.di.manga.MangaRepositoryModule
import com.gnoemes.shikimori.di.manga.MangaUtilModule
import com.gnoemes.shikimori.di.studio.StudioUtilModule
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.presentation.presenter.similar.converter.SimilarViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.similar.converter.SimilarViewModelConverterImpl
import com.gnoemes.shikimori.presentation.view.similar.SimilarFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    SimilarInteractorModule::class,
    AnimeUtilModule::class,
    AnimeRepositoryModule::class,
    MangaRepositoryModule::class,
    MangaUtilModule::class,
    StudioUtilModule::class,
    UserInteractorModule::class
])
interface SimilarModule {

    @Binds
    fun similarConverter(converter: SimilarViewModelConverterImpl): SimilarViewModelConverter

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    fun bindFragment(fragment: SimilarFragment): Fragment
}