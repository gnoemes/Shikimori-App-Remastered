package com.gnoemes.shikimori.di.app.module.local;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.gnoemes.shikimori.entity.rates.data.AnimeRateSyncDao;
import com.gnoemes.shikimori.entity.rates.data.AnimeRateSyncDaoSQLiteTypeMapping;
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
                .build();
    }

    @Provides
    @Singleton
    static SQLiteOpenHelper provideSqLiteOpenHelper(Context context) {
        return new DbOpenHelper(context);
    }
}
