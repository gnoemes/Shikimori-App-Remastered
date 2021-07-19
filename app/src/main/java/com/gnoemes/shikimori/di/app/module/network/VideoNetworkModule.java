package com.gnoemes.shikimori.di.app.module.network;

import android.content.Context;

import com.gnoemes.shikimori.BuildConfig;
import com.gnoemes.shikimori.di.app.annotations.ShimoriVideoApi;
import com.gnoemes.shikimori.di.app.annotations.VideoApi;
import com.gnoemes.shikimori.entity.app.domain.Constants;
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras;
import com.gnoemes.shikimori.utils.PreferenceKt;
import com.gnoemes.shikimori.utils.network.NetworkExtensionsKt;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public interface VideoNetworkModule {

    @Provides
    @Singleton
    @VideoApi
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
    @VideoApi
    static Retrofit.Builder provideRetrofitBuilder(@VideoApi OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    @Provides
    @Singleton
    @VideoApi
    static Retrofit provideRetrofit(@VideoApi Retrofit.Builder builder) {
        return builder.baseUrl(BuildConfig.VideoBaseUrl).build();
    }

    @Provides
    @Singleton
    @ShimoriVideoApi
    static OkHttpClient provideShimoriOkHttpClient(HttpLoggingInterceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        return NetworkExtensionsKt.enableTLS12(builder)
                .connectTimeout(Constants.LONG_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.LONG_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    @ShimoriVideoApi
    static Retrofit.Builder providePlashikiRetrofitBuilder(@ShimoriVideoApi OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    @Provides
    @Singleton
    @ShimoriVideoApi
    static Retrofit providePlashikiRetrofit(Context context,  @ShimoriVideoApi Retrofit.Builder builder) {
        String url = PreferenceKt.getDefaultSharedPreferences(context).getString(SettingsExtras.SHIMORI_URL, Constants.SHIMORI_URL);
        if (url == null) url = Constants.SHIMORI_URL;
        return builder.baseUrl(url).build();
    }
}
