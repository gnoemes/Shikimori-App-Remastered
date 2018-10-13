package com.gnoemes.shikimori.di.bottom;

import android.support.v4.app.Fragment;

import com.gnoemes.shikimori.di.base.modules.BaseFragmentModule;
import com.gnoemes.shikimori.di.base.scopes.BottomScope;
import com.gnoemes.shikimori.presentation.view.bottom.BottomTabContainer;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module(includes = BaseFragmentModule.class)
public interface BottomTabModule {

    @Binds
    @Named(BaseFragmentModule.FRAGMENT)
    @BottomScope
    Fragment bindFragment(BottomTabContainer tabContainer);
}
