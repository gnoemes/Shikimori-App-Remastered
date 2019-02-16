package com.gnoemes.shikimori.di.app.module;

import com.gnoemes.shikimori.domain.download.DownloadInteractor;
import com.gnoemes.shikimori.domain.download.DownloadInteractorImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface InteractorModule {

    @Binds
    @Reusable
    DownloadInteractor bindDownloadInteractor(DownloadInteractorImpl interactor);
}
