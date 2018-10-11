package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.common.data.GenreResponse
import com.gnoemes.shikimori.entity.common.domain.Genre
import io.reactivex.functions.Function

interface GenreResponseConverter : Function<List<GenreResponse>, List<Genre>>