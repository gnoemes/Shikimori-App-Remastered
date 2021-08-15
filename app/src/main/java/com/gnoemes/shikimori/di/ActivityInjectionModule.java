package com.gnoemes.shikimori.di;

import com.gnoemes.shikimori.di.auth.AuthModule;
import com.gnoemes.shikimori.di.base.scopes.ActivityScope;
import com.gnoemes.shikimori.di.main.module.MainModule;
import com.gnoemes.shikimori.di.player.PlayerModule;
import com.gnoemes.shikimori.presentation.view.auth.AuthActivity;
import com.gnoemes.shikimori.presentation.view.main.MainActivity;
import com.gnoemes.shikimori.presentation.view.player.embedded.EmbeddedPlayerActivity;
import com.gnoemes.shikimori.presentation.view.player.web.WebPlayerActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ActivityInjectionModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = MainModule.class)
    MainActivity mainActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = AuthModule.class)
    AuthActivity authActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = PlayerModule.class)
    EmbeddedPlayerActivity embeddedPlayerActivity();

    @ActivityScope
    @ContributesAndroidInjector
    WebPlayerActivity webPlayerActivity();
}
