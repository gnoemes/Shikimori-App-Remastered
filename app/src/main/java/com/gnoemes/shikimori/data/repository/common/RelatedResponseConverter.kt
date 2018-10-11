package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.common.data.RelatedResponse
import com.gnoemes.shikimori.entity.common.domain.Related
import io.reactivex.functions.Function

interface RelatedResponseConverter : Function<List<RelatedResponse>, List<Related>>