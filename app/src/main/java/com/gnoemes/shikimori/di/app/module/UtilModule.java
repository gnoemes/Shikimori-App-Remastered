package com.gnoemes.shikimori.di.app.module;

import android.content.Context;

import com.gnoemes.shikimori.data.repository.club.ClubResponseConverter;
import com.gnoemes.shikimori.data.repository.club.ClubResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter;
import com.gnoemes.shikimori.data.repository.common.impl.RateResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.FavoriteListResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.FavoriteListResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.MessageResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.MessageResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.UserDetailsResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.UserDetailsResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.UserHistoryConverter;
import com.gnoemes.shikimori.data.repository.user.converter.UserHistoryConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider;
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProviderImpl;
import com.gnoemes.shikimori.presentation.presenter.common.provider.ShareResourceProvider;
import com.gnoemes.shikimori.presentation.presenter.common.provider.ShareResourceProviderImpl;
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
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
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

    @Singleton
    @Provides
    static FirebaseAnalytics provideFirebaseAnalytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
    }

    @Binds
    @Reusable
    ShareResourceProvider bindShareResourceProvider(ShareResourceProviderImpl provider);

    @Binds
    @Reusable
    RateResponseConverter bindRateResponseConverter(RateResponseConverterImpl converter);

    @Binds
    @Reusable
    UserDetailsResponseConverter bindUserDetailsResponseConverter(UserDetailsResponseConverterImpl converter);

    @Binds
    @Reusable
    UserHistoryConverter bindUserHistoryConverter(UserHistoryConverterImpl converter);

    @Binds
    @Reusable
    FavoriteListResponseConverter bindFavoriteListResponseConverter(FavoriteListResponseConverterImpl converter);

    @Binds
    @Reusable
    MessageResponseConverter bindMessageResponseConverter(MessageResponseConverterImpl converter);
}
