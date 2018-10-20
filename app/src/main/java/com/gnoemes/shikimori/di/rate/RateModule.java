package com.gnoemes.shikimori.di.rate;

import com.gnoemes.shikimori.data.repository.common.RateResponseConverter;
import com.gnoemes.shikimori.data.repository.common.impl.RateResponseConverterImpl;
import com.gnoemes.shikimori.di.anime.EpisodeModule;
import com.gnoemes.shikimori.di.manga.ChapterModule;
import com.gnoemes.shikimori.domain.rates.RatesInteractor;
import com.gnoemes.shikimori.domain.rates.RatesInteractorImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module(includes = {
        ChapterModule.class,
        EpisodeModule.class
})
public interface RateModule {

    @Binds
    @Reusable
    RateResponseConverter bindRateResponseConverter(RateResponseConverterImpl responseConverter);

    @Binds
    @Reusable
    RatesInteractor bindRatesInteractor(RatesInteractorImpl interactor);

}