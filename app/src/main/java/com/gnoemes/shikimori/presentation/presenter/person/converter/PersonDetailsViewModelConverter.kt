package com.gnoemes.shikimori.presentation.presenter.person.converter

import com.gnoemes.shikimori.entity.roles.domain.PersonDetails
import io.reactivex.functions.Function

interface PersonDetailsViewModelConverter : Function<PersonDetails, List<Any>>