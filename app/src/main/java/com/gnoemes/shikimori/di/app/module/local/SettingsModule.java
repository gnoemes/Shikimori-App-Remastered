package com.gnoemes.shikimori.di.app.module.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.gnoemes.shikimori.di.app.annotations.SettingsQualifier;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public interface SettingsModule {

    @Provides
    @SettingsQualifier
    @Singleton
    static SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

//    @Binds
//    @Singleton
//    UserPreferenceSource bindUserPreferenceSource(UserPreferenceSourceImpl source);
//
//    @Binds
//    @Singleton
//    UserSettingsRepository bindUserSettingsRepository(UserSettingsRepositoryImpl repository);
//
//    @Binds
//    UserSettingsInteractor bindUserSettingsInteractor(UserSettingsInteractorImpl settingsInteractor);
//
//    @Binds
//    @Singleton
//    UserSettingsSource bindUserSettingsSource(UserSettingsSourceImpl source);
}
