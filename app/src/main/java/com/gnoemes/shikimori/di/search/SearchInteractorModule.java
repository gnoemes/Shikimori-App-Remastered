package com.gnoemes.shikimori.di.search;

import com.gnoemes.shikimori.domain.search.SearchInteractor;
import com.gnoemes.shikimori.domain.search.SearchInteractorImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface SearchInteractorModule {

    @Binds
    SearchInteractor bindSearchInteractor(SearchInteractorImpl interactor);

}
