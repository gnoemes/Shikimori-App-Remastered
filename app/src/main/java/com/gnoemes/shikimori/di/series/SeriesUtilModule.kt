package com.gnoemes.shikimori.di.series

import com.gnoemes.shikimori.data.repository.series.shikimori.converter.EpisodeResponseConverter
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.EpisodeResponseConverterImpl
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.TranslationResponseConverter
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.TranslationResponseConverterImpl
import com.gnoemes.shikimori.presentation.presenter.anime.converter.EpisodeViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.anime.converter.EpisodeViewModelConverterImpl
import com.gnoemes.shikimori.presentation.presenter.series.translations.converter.TranslationsViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.series.translations.converter.TranslationsViewModelConverterImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface SeriesUtilModule {

    @Binds
    @Reusable
    fun bindSeriesResponseConverter(converter: EpisodeResponseConverterImpl): EpisodeResponseConverter

    @Binds
    @Reusable
    fun bindTranslationResponseConverter(converter: TranslationResponseConverterImpl): TranslationResponseConverter

    @Binds
    @Reusable
    fun bindEpisodeViewModelConverter(conterter: EpisodeViewModelConverterImpl): EpisodeViewModelConverter

    @Binds
    @Reusable
    fun bindTranslationViewModelConverter(converter: TranslationsViewModelConverterImpl): TranslationsViewModelConverter

}