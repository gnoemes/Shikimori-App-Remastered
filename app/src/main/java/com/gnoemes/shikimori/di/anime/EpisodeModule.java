package com.gnoemes.shikimori.di.anime;

import com.gnoemes.shikimori.data.local.db.EpisodeDbSource;
import com.gnoemes.shikimori.data.local.db.impl.EpisodeDbSourceImpl;
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepository;
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepositoryImpl;
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.EpisodeResponseConverter;
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.EpisodeResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.TranslationResponseConverter;
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.TranslationResponseConverterImpl;
import com.gnoemes.shikimori.domain.series.SeriesInteractor;
import com.gnoemes.shikimori.domain.series.SeriesInteractorImpl;
import com.gnoemes.shikimori.presentation.presenter.anime.converter.EpisodeViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.anime.converter.EpisodeViewModelConverterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface EpisodeModule {

    @Binds
    @Reusable
    EpisodeDbSource bindEpisodeDbSource(EpisodeDbSourceImpl source);

    @Binds
    @Reusable
    SeriesInteractor bindSeriesInteractor(SeriesInteractorImpl interactor);

    @Binds
    @Reusable
    SeriesRepository bindSeriesRepository(SeriesRepositoryImpl seriesRepository);

    @Binds
    @Reusable
    EpisodeResponseConverter bindSeriesResponseConverter(EpisodeResponseConverterImpl converter);

    @Binds
    @Reusable
    TranslationResponseConverter bindTranslationResponseConverter(TranslationResponseConverterImpl converter);

    @Binds
    @Reusable
    EpisodeViewModelConverter bindEpisodeViewModelConverter(EpisodeViewModelConverterImpl conterter);

}
