package com.gnoemes.shikimori.presentation.presenter.clubs.converter

import com.gnoemes.shikimori.entity.club.presentation.UserClubViewModel
import com.gnoemes.shikimori.entity.club.domain.Club
import io.reactivex.functions.Function

interface UserClubViewModelConverter : Function<List<Club>, List<UserClubViewModel>>