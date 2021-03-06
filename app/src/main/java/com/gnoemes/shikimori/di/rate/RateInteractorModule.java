package com.gnoemes.shikimori.di.rate;

import com.gnoemes.shikimori.domain.rates.PinnedRateInteractor;
import com.gnoemes.shikimori.domain.rates.PinnedRateInteractorImpl;
import com.gnoemes.shikimori.domain.rates.RateChangesInteractor;
import com.gnoemes.shikimori.domain.rates.RateChangesInteractorImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface RateInteractorModule {

    @Binds
    @Reusable
    RateChangesInteractor bindRateChangesInteractor(RateChangesInteractorImpl interactor);

    @Binds
    @Reusable
    PinnedRateInteractor bindPinnedRateInteractor(PinnedRateInteractorImpl interactor);
}
