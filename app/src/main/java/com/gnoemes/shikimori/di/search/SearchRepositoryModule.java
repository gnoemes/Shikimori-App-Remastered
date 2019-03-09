package com.gnoemes.shikimori.di.search;

import com.gnoemes.shikimori.data.repository.search.SearchRepository;
import com.gnoemes.shikimori.data.repository.search.SearchRepositoryImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface SearchRepositoryModule {
    @Binds
    SearchRepository bindSearchRepository(SearchRepositoryImpl searchRepository);

}
