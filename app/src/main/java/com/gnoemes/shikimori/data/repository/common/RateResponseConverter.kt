package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.data.RateResponse
import com.gnoemes.shikimori.entity.rates.data.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.entity.rates.data.UserRateResponse
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.reactivex.functions.Function

interface RateResponseConverter : Function<List<RateResponse?>?, List<Rate>> {

    fun convertResponse(it: RateResponse?): Rate?

    fun convertUserRateResponse(targetId: Long?, it: UserRateResponse?): UserRate?

    fun convertCreateOrUpdateRequest(targetId: Long, type: Type, rate: UserRate, userId: Long): UserRateCreateOrUpdateRequest

    fun convertCreateOrUpdateRequest(rate: UserRate): UserRateCreateOrUpdateRequest

}