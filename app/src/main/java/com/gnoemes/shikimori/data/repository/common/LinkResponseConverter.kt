package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.common.data.LinkResponse
import com.gnoemes.shikimori.entity.common.domain.Link
import io.reactivex.functions.Function

interface LinkResponseConverter : Function<List<LinkResponse>, List<Link>>