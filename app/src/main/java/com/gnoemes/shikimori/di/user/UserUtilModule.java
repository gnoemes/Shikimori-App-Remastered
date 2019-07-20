package com.gnoemes.shikimori.di.user;

import com.gnoemes.shikimori.presentation.presenter.user.converter.UserDetailsViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.user.converter.UserDetailsViewModelConverterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface UserUtilModule {
    @Binds
    @Reusable
    UserDetailsViewModelConverter bindUserDetailsViewModelConverter(UserDetailsViewModelConverterImpl converter);
}
