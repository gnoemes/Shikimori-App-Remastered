package com.gnoemes.shikimori.di.app.module.network;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.gnoemes.shikimori.BuildConfig;
import com.gnoemes.shikimori.di.app.annotations.VideoApi;
import com.gnoemes.shikimori.entity.app.domain.Constants;
import com.gnoemes.shikimori.utils.network.NetworkExtensionsKt;
import com.gnoemes.shikimori.utils.network.PlayShikimoriConverterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public interface VideoNetworkModule {

    @Provides
    @Singleton
    @VideoApi
    static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor,
                                            @VideoApi CookieJar cookieJar) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(cookieJar);
        return NetworkExtensionsKt.enableTLS12(builder)
                .connectTimeout(Constants.LONG_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.LONG_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    @VideoApi
    static Retrofit.Builder provideRetrofitBuilder(@VideoApi Converter.Factory factory, @VideoApi OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory);
    }

    @Provides
    @Singleton
    @VideoApi
    static Retrofit provideRetrofit(@VideoApi Retrofit.Builder builder) {
        return builder.baseUrl(BuildConfig.PlaySkihimoriBaseUrl).build();
    }

    @Provides
    @Singleton
    @VideoApi
    static Converter.Factory provideFactory() {
        return new PlayShikimoriConverterFactory();
    }

    @Provides
    @Singleton
    @VideoApi
    static CookieJar provideCookieJar(Context context) {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }
}
