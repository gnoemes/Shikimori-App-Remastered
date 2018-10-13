package com.gnoemes.shikimori.di.base.modules;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.gnoemes.shikimori.di.base.scopes.BottomScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public interface BaseFragmentModule {

    String FRAGMENT = "BaseFragmentModule.fragment";

    String CHILD_FRAGMENT_MANAGER = "BaseFragmentModule.childFragmentManager";

    @Provides
    @Named(CHILD_FRAGMENT_MANAGER)
    @BottomScope
    static FragmentManager provideChildFragmentManager(@Named(FRAGMENT) Fragment fragment) {
        return fragment.getChildFragmentManager();
    }
}
