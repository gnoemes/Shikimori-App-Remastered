package com.gnoemes.shikimori.di.app.module.network;

import com.gnoemes.shikimori.BuildConfig;
import com.gnoemes.shikimori.data.local.preference.UserSource;
import com.gnoemes.shikimori.data.repository.app.AuthorizationRepository;
import com.gnoemes.shikimori.data.repository.app.TokenRepository;
import com.gnoemes.shikimori.di.app.annotations.AuthCommonApi;
import com.gnoemes.shikimori.entity.app.domain.Constants;
import com.gnoemes.shikimori.utils.network.AuthHolder;
import com.gnoemes.shikimori.utils.network.ShikiAuthenticator;
import com.gnoemes.shikimori.utils.network.TokenInterceptor;
import com.gnoemes.shikimori.utils.network.UserAgentInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public interface AuthCommonNetworkModule {

    @Provides
    @Singleton
    @AuthCommonApi
    static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor,
                                            UserAgentInterceptor userAgentInterceptor,
                                            @AuthCommonApi Authenticator authenticator,
                                            @AuthCommonApi TokenInterceptor tokenInterceptor) {
        return new OkHttpClient.Builder()
                .authenticator(authenticator)
                .addInterceptor(tokenInterceptor)
                .addInterceptor(userAgentInterceptor)
                .addInterceptor(interceptor)
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    @AuthCommonApi
    static Retrofit.Builder provideRetrofitBuilder(Converter.Factory factory, @AuthCommonApi OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory);
    }

    @Provides
    @Singleton
    @AuthCommonApi
    static Retrofit provideRetrofit(@AuthCommonApi Retrofit.Builder builder) {
        return builder.baseUrl(BuildConfig.ShikimoriBaseUrl).build();
    }

    @Provides
    @Singleton
    @AuthCommonApi
    static AuthHolder provideAuthHolder(TokenRepository tokenRepository,
                                        AuthorizationRepository repository,
                                        UserSource settingsRepository) {
        return new AuthHolder(tokenRepository, repository, settingsRepository);
    }

    @Provides
    @Singleton
    @AuthCommonApi
    static Authenticator bindShikiAuthenticator(@AuthCommonApi AuthHolder authHolder) {
        return new ShikiAuthenticator(authHolder);
    }

    @Provides
    @Singleton
    @AuthCommonApi
    static TokenInterceptor bindTokenInterceptor(TokenRepository repository) {
        return new TokenInterceptor(repository);
    }
}
