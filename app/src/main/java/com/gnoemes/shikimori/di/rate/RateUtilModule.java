package com.gnoemes.shikimori.di.rate;

import com.gnoemes.shikimori.data.repository.common.RateResponseConverter;
import com.gnoemes.shikimori.data.repository.common.impl.RateResponseConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.rates.provider.RateResourceProvider;
import com.gnoemes.shikimori.presentation.presenter.rates.provider.RateResourceProviderImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface RateUtilModule {
    @Binds
    @Reusable
    RateResponseConverter bindRateResponseConverter(RateResponseConverterImpl responseConverter);

    @Binds
    @Reusable
    RateResourceProvider bindRateResourceProvider(RateResourceProviderImpl provider);
}
