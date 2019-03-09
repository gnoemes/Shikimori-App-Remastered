package com.gnoemes.shikimori.di.search

import androidx.fragment.app.Fragment
import com.gnoemes.shikimori.data.local.services.FilterSource
import com.gnoemes.shikimori.data.local.services.impl.FilterSourceImpl
import com.gnoemes.shikimori.data.repository.search.FilterRepository
import com.gnoemes.shikimori.data.repository.search.FilterRepositoryImpl
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.domain.search.filter.FilterInteractor
import com.gnoemes.shikimori.domain.search.filter.FilterInteractorImpl
import com.gnoemes.shikimori.presentation.presenter.search.converter.FilterViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.search.converter.FilterViewModelConverterImpl
import com.gnoemes.shikimori.presentation.view.search.filter.FilterFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
interface FilterModule {

    @Binds
    fun bindFilterRepository(repository: FilterRepositoryImpl): FilterRepository

    @Binds
    fun bindFilterSource(source: FilterSourceImpl): FilterSource

    @Binds
    fun bindFilterViewModelConverter(converter: FilterViewModelConverterImpl): FilterViewModelConverter


    @Binds
    fun bindFilterInteractor(interactor: FilterInteractorImpl): FilterInteractor

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    fun bindFragment(fragment: FilterFragment): Fragment
}