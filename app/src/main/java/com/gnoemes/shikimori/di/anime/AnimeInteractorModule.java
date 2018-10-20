package com.gnoemes.shikimori.di.anime;

import com.gnoemes.shikimori.domain.anime.AnimeInteractor;
import com.gnoemes.shikimori.domain.anime.AnimeInteractorImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface AnimeInteractorModule {

    @Binds
    AnimeInteractor bindAnimeInteractor(AnimeInteractorImpl interactor);
}
