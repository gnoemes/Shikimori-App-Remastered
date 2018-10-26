package com.gnoemes.shikimori.presentation.presenter.character.converter

import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import io.reactivex.functions.Function

interface CharacterDetailsViewModelConverter : Function<CharacterDetails, List<Any>>