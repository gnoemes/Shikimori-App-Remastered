package com.gnoemes.shikimori.di.calendar;

import androidx.fragment.app.Fragment;

import com.arellomobile.mvp.MvpPresenter;
import com.gnoemes.shikimori.data.repository.calendar.CalendarRepository;
import com.gnoemes.shikimori.data.repository.calendar.CalendarRepositoryImpl;
import com.gnoemes.shikimori.data.repository.calendar.converter.CalendarResponseConverter;
import com.gnoemes.shikimori.data.repository.calendar.converter.CalendarResponseConverterImpl;
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule;
import com.gnoemes.shikimori.di.base.scopes.BottomScope;
import com.gnoemes.shikimori.di.search.SearchRepositoryModule;
import com.gnoemes.shikimori.di.search.SearchUtilModule;
import com.gnoemes.shikimori.di.user.UserUtilModule;
import com.gnoemes.shikimori.domain.calendar.CalendarInteractor;
import com.gnoemes.shikimori.domain.calendar.CalendarInteractorImpl;
import com.gnoemes.shikimori.presentation.presenter.calendar.CalendarPresenter;
import com.gnoemes.shikimori.presentation.presenter.calendar.converter.CalendarViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.calendar.converter.CalendarViewModelConverterImpl;
import com.gnoemes.shikimori.presentation.view.calendar.CalendarFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module(includes = {BaseChildFragmentModule.class,
        SearchRepositoryModule.class,
        SearchUtilModule.class,
        UserUtilModule.class
})
public interface CalendarModule {

    @Binds
    MvpPresenter bindPresenter(CalendarPresenter presenter);

    @Binds
    CalendarInteractor bindCalendarInteractor(CalendarInteractorImpl interactor);

    @Binds
    CalendarRepository bindCalendarRepository(CalendarRepositoryImpl repository);

    @Binds
    CalendarResponseConverter bindCalendarResponseConverter(CalendarResponseConverterImpl converter);

    @Binds
    CalendarViewModelConverter bindCalendarViewModelConverter(CalendarViewModelConverterImpl converter);

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomScope
    Fragment bindFragment(CalendarFragment calendarFragment);
}
