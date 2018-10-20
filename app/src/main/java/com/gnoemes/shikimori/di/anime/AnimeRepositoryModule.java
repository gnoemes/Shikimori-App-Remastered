package com.gnoemes.shikimori.di.anime;

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource;
import com.gnoemes.shikimori.data.local.db.impl.AnimeRateSyncDbSourceImpl;
import com.gnoemes.shikimori.data.repository.anime.AnimeRepository;
import com.gnoemes.shikimori.data.repository.anime.AnimeRepositoryImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface AnimeRepositoryModule {

    @Binds
    AnimeRepository bindAnimeRepository(AnimeRepositoryImpl animeRepository);

    @Binds
    AnimeRateSyncDbSource bindAnimeRateSyncDbSource(AnimeRateSyncDbSourceImpl source);
}
