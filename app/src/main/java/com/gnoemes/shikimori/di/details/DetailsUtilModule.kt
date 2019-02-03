package com.gnoemes.shikimori.di.details

import com.gnoemes.shikimori.presentation.presenter.common.converter.*
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface DetailsUtilModule {

    @Binds
    @Reusable
    fun bindLinkViewModelConverter(converter: LinkViewModelConverterImpl): LinkViewModelConverter

    @Binds
    @Reusable
    fun bindFranchiseNodeViewModelConverter(converter: FranchiseNodeViewModelConverterImpl): FranchiseNodeViewModelConverter

    @Binds
    @Reusable
    fun bindDetailsContentViewModelConverter(converter: DetailsContentViewModelConverterImpl): DetailsContentViewModelConverter
}