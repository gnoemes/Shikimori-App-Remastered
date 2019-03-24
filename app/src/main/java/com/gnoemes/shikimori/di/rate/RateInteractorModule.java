package com.gnoemes.shikimori.di.rate;

import com.gnoemes.shikimori.domain.rates.RateChangesInteractor;
import com.gnoemes.shikimori.domain.rates.RateChangesInteractorImpl;
import com.gnoemes.shikimori.domain.rates.RatesInteractor;
import com.gnoemes.shikimori.domain.rates.RatesInteractorImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface RateInteractorModule {
    @Binds
    @Reusable
    RatesInteractor bindRatesInteractor(RatesInteractorImpl interactor);

    @Binds
    @Reusable
    RateChangesInteractor bindRateChangesInteractor(RateChangesInteractorImpl interactor);
}
