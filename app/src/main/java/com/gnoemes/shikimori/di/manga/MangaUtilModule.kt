package com.gnoemes.shikimori.di.manga

import com.gnoemes.shikimori.data.repository.manga.converter.MangaDetailsResponseConverter
import com.gnoemes.shikimori.data.repository.manga.converter.MangaDetailsResponseConverterImpl
import com.gnoemes.shikimori.di.details.DetailsUtilModule
import com.gnoemes.shikimori.presentation.presenter.manga.converter.MangaDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.manga.converter.MangaDetailsViewModelConverterImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module(includes = [DetailsUtilModule::class])
interface MangaUtilModule {

    @Binds
    @Reusable
    fun bindMangaViewModelConverter(converter : MangaDetailsViewModelConverterImpl) : MangaDetailsViewModelConverter

    @Binds
    @Reusable
    fun bindMangaDetailsResponseConverter(responseConverter: MangaDetailsResponseConverterImpl): MangaDetailsResponseConverter

}