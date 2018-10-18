package com.gnoemes.shikimori.di.auth;

import com.arellomobile.mvp.MvpPresenter;
import com.gnoemes.shikimori.di.base.modules.BaseActivityModule;
import com.gnoemes.shikimori.di.base.scopes.ActivityScope;
import com.gnoemes.shikimori.di.user.UserUtilModule;
import com.gnoemes.shikimori.domain.auth.AuthInteractor;
import com.gnoemes.shikimori.domain.auth.AuthInteractorImpl;
import com.gnoemes.shikimori.presentation.presenter.auth.AuthPresenter;
import com.gnoemes.shikimori.presentation.view.auth.AuthActivity;

import androidx.appcompat.app.AppCompatActivity;
import dagger.Binds;
import dagger.Module;

@Module(includes = {BaseActivityModule.class,
        UserUtilModule.class})
public interface AuthModule {

    @Binds
    MvpPresenter bindPresenter(AuthPresenter presenter);

    @Binds
    AuthInteractor bindAuthInteractor(AuthInteractorImpl interactor);

    @Binds
    @ActivityScope
    AppCompatActivity bindAppCompatActivity(AuthActivity activity);
}
