package com.gnoemes.shikimori.di.user;

import com.gnoemes.shikimori.domain.user.UserInteractor;
import com.gnoemes.shikimori.domain.user.UserInteractorImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface UserInteractorModule {

    @Binds
    @Reusable
    UserInteractor bindUserInteractor(UserInteractorImpl interactor);
}
