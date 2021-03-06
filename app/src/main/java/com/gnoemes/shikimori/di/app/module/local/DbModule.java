package com.gnoemes.shikimori.di.app.module.local;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.gnoemes.shikimori.entity.chapters.ChapterDao;
import com.gnoemes.shikimori.entity.chapters.ChapterDaoSQLiteTypeMapping;
import com.gnoemes.shikimori.entity.rates.data.AnimeRateSyncDao;
import com.gnoemes.shikimori.entity.rates.data.AnimeRateSyncDaoSQLiteTypeMapping;
import com.gnoemes.shikimori.entity.rates.data.MangaRateSyncDao;
import com.gnoemes.shikimori.entity.rates.data.MangaRateSyncDaoSQLiteTypeMapping;
import com.gnoemes.shikimori.entity.rates.data.PinnedRateDao;
import com.gnoemes.shikimori.entity.rates.data.PinnedRateDaoSQLiteTypeMapping;
import com.gnoemes.shikimori.entity.series.data.EpisodeDao;
import com.gnoemes.shikimori.entity.series.data.EpisodeDaoSQLiteTypeMapping;
import com.gnoemes.shikimori.entity.series.data.TranslationSettingDao;
import com.gnoemes.shikimori.entity.series.data.TranslationSettingDaoSQLiteTypeMapping;
import com.gnoemes.shikimori.utils.db.DbOpenHelper;
import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public interface DbModule {

    @Provides
    @Singleton
    static StorIOSQLite provideStorIosqLite(@NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(AnimeRateSyncDao.class, new AnimeRateSyncDaoSQLiteTypeMapping())
                .addTypeMapping(EpisodeDao.class, new EpisodeDaoSQLiteTypeMapping())
                .addTypeMapping(MangaRateSyncDao.class, new MangaRateSyncDaoSQLiteTypeMapping())
                .addTypeMapping(TranslationSettingDao.class, new TranslationSettingDaoSQLiteTypeMapping())
                .addTypeMapping(ChapterDao.class, new ChapterDaoSQLiteTypeMapping())
                .addTypeMapping(PinnedRateDao.class, new PinnedRateDaoSQLiteTypeMapping())
                .build();
    }

    @Provides
    @Singleton
    static SQLiteOpenHelper provideSqLiteOpenHelper(Context context) {
        return new DbOpenHelper(context);
    }
}
