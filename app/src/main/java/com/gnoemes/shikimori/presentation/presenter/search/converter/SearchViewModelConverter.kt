package com.gnoemes.shikimori.presentation.presenter.search.converter

import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import io.reactivex.functions.Function

interface SearchViewModelConverter : Function<List<Any>, List<SearchItem>>