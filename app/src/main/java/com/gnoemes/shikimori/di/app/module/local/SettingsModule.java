package com.gnoemes.shikimori.di.app.module.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.gnoemes.shikimori.data.local.preference.SettingsSource;
import com.gnoemes.shikimori.data.local.preference.PlayerSettingsSource;
import com.gnoemes.shikimori.data.local.preference.impl.SettingsSourceImpl;
import com.gnoemes.shikimori.data.local.preference.UserSource;
import com.gnoemes.shikimori.data.local.preference.impl.UserSourceImpl;
import com.gnoemes.shikimori.data.local.preference.impl.PlayerSettingsSourceImpl;
import com.gnoemes.shikimori.di.app.annotations.SettingsQualifier;
import com.gnoemes.shikimori.di.app.annotations.UserQualifier;

import javax.inject.Singleton;

import androidx.preference.PreferenceManager;
import dagger.Binds;
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

    @Provides
    @UserQualifier
    @Singleton
    static SharedPreferences provideUserQualifierSharedPreferences(Context context) {
        return context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
    }

    @Binds
    @Singleton
    UserSource bindUserSource(UserSourceImpl source);

    @Binds
    @Singleton
    SettingsSource bindSettingsSource(SettingsSourceImpl source);

    @Binds
    @Singleton
    PlayerSettingsSource bindVideoSettingsSource(PlayerSettingsSourceImpl source);
}
