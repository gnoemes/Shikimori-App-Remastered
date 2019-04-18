package com.gnoemes.shikimori.di.app.module.network;

import com.gnoemes.shikimori.di.app.annotations.DocumentVideoApi;
import com.gnoemes.shikimori.entity.app.domain.Constants;
import com.gnoemes.shikimori.utils.network.NetworkExtensionsKt;
import com.gnoemes.shikimori.utils.network.PlayShikimoriConverterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public interface DocumentVideoNetworkModule {

    @Provides
    @Singleton
    @DocumentVideoApi
    static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        return NetworkExtensionsKt.enableTLS12(builder)
                .connectTimeout(Constants.LONG_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.LONG_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    @DocumentVideoApi
    static Retrofit provideRetrofit(@DocumentVideoApi Retrofit.Builder builder) {
        return builder.baseUrl("https://play.shikimori.org").build();
    }

    @Provides
    @Singleton
    @DocumentVideoApi
    static Converter.Factory provideFactory() {
        return new PlayShikimoriConverterFactory();
    }

    @Provides
    @Singleton
    @DocumentVideoApi
    static Retrofit.Builder provideRetrofitBuilder(@DocumentVideoApi OkHttpClient client, @DocumentVideoApi Converter.Factory converterFactory) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converterFactory);
    }
}
