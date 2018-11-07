package com.gnoemes.shikimori.di.app.module;

import com.gnoemes.shikimori.data.repository.club.ClubResponseConverter;
import com.gnoemes.shikimori.data.repository.club.ClubResponseConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider;
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProviderImpl;
import com.gnoemes.shikimori.presentation.presenter.common.provider.SortResourceProvider;
import com.gnoemes.shikimori.presentation.presenter.common.provider.SortResourceProviderImpl;
import com.gnoemes.shikimori.utils.date.DateTimeConverter;
import com.gnoemes.shikimori.utils.date.DateTimeResourceProvider;
import com.gnoemes.shikimori.utils.date.DateTimeUtils;
import com.gnoemes.shikimori.utils.date.impl.DateTimeConverterImpl;
import com.gnoemes.shikimori.utils.date.impl.DateTimeResourceProviderImpl;
import com.gnoemes.shikimori.utils.date.impl.DateTimeUtilsImpl;
import com.gnoemes.shikimori.utils.images.GlideImageLoader;
import com.gnoemes.shikimori.utils.images.ImageLoader;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface UtilModule {

    @Binds
    @Singleton
    ImageLoader bindImageLoader(GlideImageLoader loader);

    @Binds
    @Reusable
    DateTimeConverter bindDateTimeConverter(DateTimeConverterImpl converter);

    @Binds
    @Reusable
    DateTimeUtils bindDateTimeUtils(DateTimeUtilsImpl utils);

    @Binds
    @Reusable
    DateTimeResourceProvider bindDateTimeResourceProvider(DateTimeResourceProviderImpl provider);

    @Binds
    @Reusable
    ClubResponseConverter bindClubResponseConverter(ClubResponseConverterImpl converter);

    @Binds
    @Reusable
    CommonResourceProvider bindCommonResourceProvider(CommonResourceProviderImpl provider);

    @Binds
    @Reusable
    SortResourceProvider bindSortResourceProvider(SortResourceProviderImpl provider);
}
