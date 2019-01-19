package com.gnoemes.shikimori.di.rate;

import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider;
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProviderImpl;
import com.gnoemes.shikimori.presentation.presenter.rates.provider.RateResourceProvider;
import com.gnoemes.shikimori.presentation.presenter.rates.provider.RateResourceProviderImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface RateUtilModule {

    @Binds
    @Reusable
    RateResourceProvider bindRateResourceProvider(RateResourceProviderImpl provider);

    @Binds
    @Reusable
    RatingResourceProvider bindRatingResourceProvider(RatingResourceProviderImpl provider);
}
