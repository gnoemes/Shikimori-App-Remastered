package com.gnoemes.shikimori.di.app.module;

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource;
import com.gnoemes.shikimori.data.local.db.ChapterDbSource;
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource;
import com.gnoemes.shikimori.data.local.db.MangaRateSyncDbSource;
import com.gnoemes.shikimori.data.local.db.impl.AnimeRateSyncDbSourceImpl;
import com.gnoemes.shikimori.data.local.db.impl.ChapterDbSourceImpl;
import com.gnoemes.shikimori.data.local.db.impl.EpisodeDbSourceImpl;
import com.gnoemes.shikimori.data.local.db.impl.MangaRateSyncDbSourceImpl;
import com.gnoemes.shikimori.data.local.services.DownloadSource;
import com.gnoemes.shikimori.data.local.services.impl.DownloadManagerSourceImpl;
import com.gnoemes.shikimori.data.repository.app.AnalyticRepository;
import com.gnoemes.shikimori.data.repository.app.AuthorizationRepository;
import com.gnoemes.shikimori.data.repository.app.TaskRepository;
import com.gnoemes.shikimori.data.repository.app.TokenRepository;
import com.gnoemes.shikimori.data.repository.app.TokenSource;
import com.gnoemes.shikimori.data.repository.app.impl.AuthorizationRepositoryImpl;
import com.gnoemes.shikimori.data.repository.app.impl.FirebaseAnalyticRepositoryImpl;
import com.gnoemes.shikimori.data.repository.app.impl.TaskRepostioryImpl;
import com.gnoemes.shikimori.data.repository.app.impl.TokenRepositoryImpl;
import com.gnoemes.shikimori.data.repository.app.impl.TokenSourceImpl;
import com.gnoemes.shikimori.data.repository.download.DownloadRepository;
import com.gnoemes.shikimori.data.repository.download.DownloadRepositoryImpl;
import com.gnoemes.shikimori.data.repository.rates.RateChangesRepository;
import com.gnoemes.shikimori.data.repository.rates.RateChangesRepositoryImpl;
import com.gnoemes.shikimori.data.repository.rates.RatesRepository;
import com.gnoemes.shikimori.data.repository.rates.RatesRepositoryImpl;
import com.gnoemes.shikimori.data.repository.series.shikimori.EpisodeChangesRepository;
import com.gnoemes.shikimori.data.repository.series.shikimori.EpisodeChangesRepositoryImpl;
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesSyncRepository;
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesSyncRepositoryImpl;
import com.gnoemes.shikimori.data.repository.series.smotretanime.Anime365TokenSource;
import com.gnoemes.shikimori.data.repository.series.smotretanime.Anime365TokenSourceImpl;
import com.gnoemes.shikimori.data.repository.user.UserRepository;
import com.gnoemes.shikimori.data.repository.user.UserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface RepositoryModule {

    @Binds
    @Singleton
    TokenRepository bindTokenRepository(TokenRepositoryImpl repository);

    @Binds
    @Singleton
    TokenSource bindTokenSource(TokenSourceImpl source);

    @Binds
    @Reusable
    UserRepository bindUserRepository(UserRepositoryImpl repository);

    @Binds
    @Reusable
    RatesRepository bindRatesRepository(RatesRepositoryImpl repository);

    @Binds
    @Reusable
    AuthorizationRepository bindAuthorizationRepository(AuthorizationRepositoryImpl repository);

    @Binds
    @Singleton
    EpisodeChangesRepository bindEpisodeChangesRepository(EpisodeChangesRepositoryImpl repository);

    @Binds
    @Singleton
    DownloadSource bindDownloadSource(DownloadManagerSourceImpl source);

    @Binds
    @Reusable
    DownloadRepository bindDownloadRepository(DownloadRepositoryImpl repository);

    @Binds
    @Singleton
    AnalyticRepository bindAnalyticRepository(FirebaseAnalyticRepositoryImpl repository);

    @Binds
    @Singleton
    RateChangesRepository bindRateChangesRepository(RateChangesRepositoryImpl repository);

    @Binds
    @Singleton
    TaskRepository bindTaskRepository(TaskRepostioryImpl repostiory);

    @Binds
    @Singleton
    SeriesSyncRepository bindSeriesSyncRepository(SeriesSyncRepositoryImpl repository);

    @Binds
    @Singleton
    EpisodeDbSource bindEpisodeDbSource(EpisodeDbSourceImpl source);

    @Binds
    @Singleton
    ChapterDbSource bindChapterDbSource(ChapterDbSourceImpl source);

    @Binds
    @Singleton
    AnimeRateSyncDbSource bindAnimeRateSyncDbSource(AnimeRateSyncDbSourceImpl source);

    @Binds
    @Reusable
    MangaRateSyncDbSource bindMangaRateSyncDbSource(MangaRateSyncDbSourceImpl source);

    @Binds
    @Singleton
    Anime365TokenSource bindAnime365TokenSource(Anime365TokenSourceImpl source);
}
