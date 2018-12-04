package com.gnoemes.shikimori.di.character;

import com.arellomobile.mvp.MvpPresenter;
import com.gnoemes.shikimori.data.repository.roles.CharacterRepository;
import com.gnoemes.shikimori.data.repository.roles.CharacterRepositoryImpl;
import com.gnoemes.shikimori.data.repository.roles.converter.CharacterDetailsResponseConverter;
import com.gnoemes.shikimori.data.repository.roles.converter.CharacterDetailsResponseConverterImpl;
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule;
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.domain.roles.CharacterInteractor;
import com.gnoemes.shikimori.domain.roles.CharacterInteractorImpl;
import com.gnoemes.shikimori.presentation.presenter.character.CharacterPresenter;
import com.gnoemes.shikimori.presentation.presenter.character.converter.CharacterDetailsViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.character.converter.CharacterDetailsViewModelConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverterImpl;
import com.gnoemes.shikimori.presentation.view.character.CharacterFragment;

import javax.inject.Named;

import androidx.fragment.app.Fragment;
import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module(includes = {
        BaseChildFragmentModule.class,
})
public interface CharacterModule {

    @Binds
    CharacterInteractor bindCharacterInteractor(CharacterInteractorImpl interactor);

    @Binds
    CharacterRepository bindCharacterRepository(CharacterRepositoryImpl repository);

    @Binds
    CharacterDetailsResponseConverter bindCharacterDetailsResponseConverter(CharacterDetailsResponseConverterImpl converter);

    @Binds
    CharacterDetailsViewModelConverter bindCharacterDetailsViewModelConterter(CharacterDetailsViewModelConverterImpl conterter);

    @Binds
    @Reusable
    DetailsContentViewModelConverter bindDetailsContentViewModelConverter(DetailsContentViewModelConverterImpl converter);

    @Binds
    MvpPresenter bindPresenter(CharacterPresenter presenter);

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    Fragment bindFragment(CharacterFragment fragment);
}
