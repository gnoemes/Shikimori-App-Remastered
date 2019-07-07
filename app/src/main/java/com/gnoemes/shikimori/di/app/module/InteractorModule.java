package com.gnoemes.shikimori.di.app.module;

import com.gnoemes.shikimori.domain.app.AnalyticInteractor;
import com.gnoemes.shikimori.domain.app.AnalyticInteractorImpl;
import com.gnoemes.shikimori.domain.app.CancelableTaskInteractor;
import com.gnoemes.shikimori.domain.app.CancelableTaskInteractorImpl;
import com.gnoemes.shikimori.domain.download.DownloadInteractor;
import com.gnoemes.shikimori.domain.download.DownloadInteractorImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface InteractorModule {

    @Binds
    @Reusable
    DownloadInteractor bindDownloadInteractor(DownloadInteractorImpl interactor);

    @Binds
    @Singleton
    AnalyticInteractor bindAnalyticInteractor(AnalyticInteractorImpl interactor);

    @Binds
    @Singleton
    CancelableTaskInteractor bindCancelableTaskInteractor(CancelableTaskInteractorImpl interactor);
}
