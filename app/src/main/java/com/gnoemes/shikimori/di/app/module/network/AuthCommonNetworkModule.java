package com.gnoemes.shikimori.di.app.module.network;

import dagger.Module;

@Module
interface AuthCommonNetworkModule {
//
//    @Provides
//    @Singleton
//    @AuthCommonApi
//    static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor,
//                                            UserAgentInterceptor userAgentInterceptor,
//                                            @AuthCommonApi Authenticator authenticator,
//                                            @AuthCommonApi TokenInterceptor tokenInterceptor) {
//        return new OkHttpClient.Builder()
//                .authenticator(authenticator)
//                .addInterceptor(tokenInterceptor)
//                .addInterceptor(userAgentInterceptor)
//                .addInterceptor(interceptor)
//                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .build();
//    }
//
//    @Provides
//    @Singleton
//    @AuthCommonApi
//    static Retrofit.Builder provideRetrofitBuilder(Converter.Factory factory, @AuthCommonApi OkHttpClient client) {
//        return new Retrofit.Builder()
//                .client(client)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(factory);
//    }
//
//    @Provides
//    @Singleton
//    @AuthCommonApi
//    static Retrofit provideRetrofit(@AuthCommonApi Retrofit.Builder builder) {
//        return builder.baseUrl(BuildConfig.ShikimoriBaseUrl).build();
//    }
//
//    @Provides
//    @Singleton
//    @AuthCommonApi
//    static AuthHolder provideAuthHolder(TokenRepository tokenRepository,
//                                        AuthorizationRepository repository,
//                                        UserSettingsRepository settingsRepository) {
//        return new AuthHolder(tokenRepository, repository, settingsRepository);
//    }
//
//    @Provides
//    @Singleton
//    @AuthCommonApi
//    static Authenticator bindShikiAuthenticator(@AuthCommonApi AuthHolder authHolder) {
//        return new ShikiAuthenticator(authHolder);
//    }
//
//    @Provides
//    @Singleton
//    @AuthCommonApi
//    static TokenInterceptor bindTokenInterceptor(TokenRepository repository) {
//        return new TokenInterceptor(repository);
//    }
}
