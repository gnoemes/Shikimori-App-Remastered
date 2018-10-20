package com.gnoemes.shikimori.di.bottom;

import com.gnoemes.shikimori.di.FragmentInjectionModule;
import com.gnoemes.shikimori.di.anime.AnimeModule;
import com.gnoemes.shikimori.di.base.modules.BaseFragmentModule;
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.di.base.scopes.BottomScope;
import com.gnoemes.shikimori.presentation.view.anime.AnimeFragment;
import com.gnoemes.shikimori.presentation.view.bottom.BottomTabContainer;

import javax.inject.Named;

import androidx.fragment.app.Fragment;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = {BaseFragmentModule.class, FragmentInjectionModule.class})
public interface BottomTabModule {

    @BottomChildScope
    @ContributesAndroidInjector(modules = {AnimeModule.class})
    AnimeFragment animeFragmentInjector();

    @Binds
    @Named(BaseFragmentModule.FRAGMENT)
    @BottomScope
    Fragment bindFragment(BottomTabContainer tabContainer);
}
