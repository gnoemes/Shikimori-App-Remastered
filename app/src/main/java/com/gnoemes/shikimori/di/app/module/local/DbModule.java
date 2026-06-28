package com.gnoemes.shikimori.di.app.module.local;

import android.content.Context;

import androidx.room.Room;

import com.gnoemes.shikimori.data.local.db.AppDatabase;
import com.gnoemes.shikimori.data.local.db.dao.AnimeRateSyncRoomDao;
import com.gnoemes.shikimori.data.local.db.dao.ChapterRoomDao;
import com.gnoemes.shikimori.data.local.db.dao.EpisodeRoomDao;
import com.gnoemes.shikimori.data.local.db.dao.MangaRateSyncRoomDao;
import com.gnoemes.shikimori.data.local.db.dao.PinnedRateRoomDao;
import com.gnoemes.shikimori.data.local.db.dao.TranslationSettingRoomDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public interface DbModule {

    @Provides
    @Singleton
    static AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "shikimori_database")
                .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3)
                .build();
    }

    @Provides
    @Singleton
    static AnimeRateSyncRoomDao provideAnimeRateSyncDao(AppDatabase database) {
        return database.animeRateSyncDao();
    }

    @Provides
    @Singleton
    static EpisodeRoomDao provideEpisodeDao(AppDatabase database) {
        return database.episodeDao();
    }

    @Provides
    @Singleton
    static MangaRateSyncRoomDao provideMangaRateSyncDao(AppDatabase database) {
        return database.mangaRateSyncDao();
    }

    @Provides
    @Singleton
    static TranslationSettingRoomDao provideTranslationSettingDao(AppDatabase database) {
        return database.translationSettingDao();
    }

    @Provides
    @Singleton
    static ChapterRoomDao provideChapterDao(AppDatabase database) {
        return database.chapterDao();
    }

    @Provides
    @Singleton
    static PinnedRateRoomDao providePinnedRateDao(AppDatabase database) {
        return database.pinnedRateDao();
    }
}
