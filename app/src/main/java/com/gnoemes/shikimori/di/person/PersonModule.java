package com.gnoemes.shikimori.di.person;

import com.arellomobile.mvp.MvpPresenter;
import com.gnoemes.shikimori.data.repository.roles.PersonRepository;
import com.gnoemes.shikimori.data.repository.roles.PersonRepositoryImpl;
import com.gnoemes.shikimori.data.repository.roles.converter.PersonDetailsResponseConverter;
import com.gnoemes.shikimori.data.repository.roles.converter.PersonDetailsResponseConverterImpl;
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule;
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.domain.roles.PersonInteractor;
import com.gnoemes.shikimori.domain.roles.PersonInteractorImpl;
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.person.PersonPresenter;
import com.gnoemes.shikimori.presentation.presenter.person.converter.PersonDetailsViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.person.converter.PersonDetailsViewModelConverterImpl;
import com.gnoemes.shikimori.presentation.view.person.PersonFragment;

import javax.inject.Named;

import androidx.fragment.app.Fragment;
import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module(includes = {
        BaseChildFragmentModule.class,
})
public interface PersonModule {

    @Binds
    PersonInteractor bindPersonInteractor(PersonInteractorImpl interactor);

    @Binds
    PersonRepository bindPersonRepository(PersonRepositoryImpl repository);

    @Binds
    PersonDetailsResponseConverter bindPersonDetailsResponseConverter(PersonDetailsResponseConverterImpl converter);

    @Binds
    PersonDetailsViewModelConverter bindPersonDetailsViewModelConverter(PersonDetailsViewModelConverterImpl conterter);

    @Binds
    @Reusable
    DetailsContentViewModelConverter bindDetailsContentViewModelConverter(DetailsContentViewModelConverterImpl converter);

    @Binds
    MvpPresenter bindPresenter(PersonPresenter presenter);

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    Fragment bindFragment(PersonFragment fragment);
}