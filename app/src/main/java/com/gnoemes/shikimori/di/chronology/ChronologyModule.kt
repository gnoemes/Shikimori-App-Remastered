package com.gnoemes.shikimori.di.chronology

import com.gnoemes.shikimori.di.anime.AnimeRepositoryModule
import com.gnoemes.shikimori.di.anime.AnimeUtilModule
import com.gnoemes.shikimori.di.manga.MangaRepositoryModule
import com.gnoemes.shikimori.di.manga.MangaUtilModule
import com.gnoemes.shikimori.di.search.SearchRepositoryModule
import com.gnoemes.shikimori.di.search.SearchUtilModule
import com.gnoemes.shikimori.di.studio.StudioUtilModule
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.domain.chronology.ChronologyInteractor
import com.gnoemes.shikimori.domain.chronology.ChronologyInteratorImpl
import com.gnoemes.shikimori.presentation.presenter.chronology.converter.ChronologyViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.chronology.converter.ChronologyViewModelConverterImpl
import dagger.Binds
import dagger.Module

@Module(includes = [
    AnimeUtilModule::class,
    AnimeRepositoryModule::class,
    MangaRepositoryModule::class,
    MangaUtilModule::class,
    StudioUtilModule::class,
    UserInteractorModule::class,
    SearchRepositoryModule::class,
    SearchUtilModule::class
])
interface ChronologyModule {

    @Binds
    fun bindConverter(converter: ChronologyViewModelConverterImpl): ChronologyViewModelConverter

    @Binds
    fun bindInteractor(interactor: ChronologyInteratorImpl): ChronologyInteractor
}