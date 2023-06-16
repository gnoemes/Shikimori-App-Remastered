package com.gnoemes.shikimori.di.app.module.network;

import com.gnoemes.shikimori.data.network.AnimeApi;
import com.gnoemes.shikimori.data.network.AuthApi;
import com.gnoemes.shikimori.data.network.CalendarApi;
import com.gnoemes.shikimori.data.network.CommentApi;
import com.gnoemes.shikimori.data.network.DocumentVideoApi;
import com.gnoemes.shikimori.data.network.MangaApi;
import com.gnoemes.shikimori.data.network.ShikicinemaVideoApi;
import com.gnoemes.shikimori.data.network.ShimoriVideoApi;
import com.gnoemes.shikimori.data.network.RanobeApi;
import com.gnoemes.shikimori.data.network.RolesApi;
import com.gnoemes.shikimori.data.network.TopicApi;
import com.gnoemes.shikimori.data.network.UserApi;
import com.gnoemes.shikimori.data.network.VideoApi;
import com.gnoemes.shikimori.di.app.annotations.AuthCommonApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = {RetrofitModule.class, CommonNetworkModule.class, VideoNetworkModule.class,
        AuthCommonNetworkModule.class, DocumentVideoNetworkModule.class})
public interface ApiModule {

    @Singleton
    @Provides
    static CalendarApi bindCalendarApi(Retrofit retrofit) {
        return retrofit.create(CalendarApi.class);
    }

    @Singleton
    @Provides
    static AnimeApi bindAnimesApi(@AuthCommonApi Retrofit retrofit) {
        return retrofit.create(AnimeApi.class);
    }

    @Singleton
    @Provides
    static VideoApi bindVideoApi(@com.gnoemes.shikimori.di.app.annotations.VideoApi Retrofit retrofit) {
        return retrofit.create(VideoApi.class);
    }

    @Singleton
    @Provides
    static DocumentVideoApi bindDocumentVideoApi(@com.gnoemes.shikimori.di.app.annotations.DocumentVideoApi Retrofit retrofit) {
        return retrofit.create(DocumentVideoApi.class);
    }

    @Singleton
    @Provides
    static ShimoriVideoApi bindShimoriVideoApi(@com.gnoemes.shikimori.di.app.annotations.ShimoriVideoApi Retrofit retrofit) {
        return retrofit.create(ShimoriVideoApi.class);
    }

    @Singleton
    @Provides
    static ShikicinemaVideoApi bindShikicinemaVideoApi(@com.gnoemes.shikimori.di.app.annotations.ShikicinemaVideoApi Retrofit retrofit) {
        return retrofit.create(ShikicinemaVideoApi.class);
    }

    @Singleton
    @Provides
    static AuthApi bindAuthApi(Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }

    @Singleton
    @Provides
    static UserApi bindUserApi(@AuthCommonApi Retrofit retrofit) {
        return retrofit.create(UserApi.class);
    }

    @Singleton
    @Provides
    static CommentApi bindCommentsApi(Retrofit retrofit) {
        return retrofit.create(CommentApi.class);
    }

    @Singleton
    @Provides
    static RolesApi bindCharactersApi(Retrofit retrofit) {
        return retrofit.create(RolesApi.class);
    }

    @Singleton
    @Provides
    static TopicApi bindTopicApi(@AuthCommonApi Retrofit retrofit) {
        return retrofit.create(TopicApi.class);
    }

    @Singleton
    @Provides
    static MangaApi bindMangaApi(@AuthCommonApi Retrofit retrofit) {
        return retrofit.create(MangaApi.class);
    }

    @Singleton
    @Provides
    static RanobeApi bindRanobeApi(@AuthCommonApi Retrofit retrofit) {
        return retrofit.create(RanobeApi.class);
    }
}
