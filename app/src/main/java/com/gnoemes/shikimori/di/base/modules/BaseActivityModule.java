package com.gnoemes.shikimori.di.base.modules;

import android.app.Activity;
import android.content.Context;

import com.gnoemes.shikimori.di.base.scopes.ActivityScope;
import com.gnoemes.shikimori.di.main.qualifier.ActivityContext;

import javax.inject.Named;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public interface BaseActivityModule {

    String ACTIVITY_FRAGMENT_MANAGER = "BaseActivityModule.activityFragmentManager";

    @Provides
    @Named(ACTIVITY_FRAGMENT_MANAGER)
    @ActivityScope
    static FragmentManager provideActivityFragmentManager(AppCompatActivity appCompatActivity) {
        return appCompatActivity.getSupportFragmentManager();
    }

    @Binds
    @ActivityScope
    Activity activity(AppCompatActivity appCompatActivity);

    @Binds
    @ActivityScope
    @ActivityContext
    Context bindContext(Activity activity);
}
