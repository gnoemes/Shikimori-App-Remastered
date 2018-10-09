package com.gnoemes.shikimori.di.app.module.network;

import com.gnoemes.shikimori.BuildConfig;
import com.gnoemes.shikimori.di.app.annotations.AlternativeApi;
import com.gnoemes.shikimori.entity.app.domain.Constants;

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
public interface AlternativeNetworkModule {

    @Provides
    @Singleton
    @AlternativeApi
    static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    @AlternativeApi
    static Retrofit.Builder provideRetrofitBuilder(Converter.Factory factory, @AlternativeApi OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory);
    }

    @Provides
    @Singleton
    @AlternativeApi
    static Retrofit provideRetrofit(@AlternativeApi Retrofit.Builder builder) {
        return builder.baseUrl(BuildConfig.AlternativeBaseUrl).build();
    }
}
