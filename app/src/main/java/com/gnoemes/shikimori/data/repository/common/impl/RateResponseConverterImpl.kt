package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter
import com.gnoemes.shikimori.data.repository.common.MangaResponseConverter
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.data.RateResponse
import com.gnoemes.shikimori.entity.rates.data.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.entity.rates.data.UserRateResponse
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import javax.inject.Inject

class RateResponseConverterImpl @Inject constructor(
        private val animeConverter: AnimeResponseConverter,
        private val mangaConverter: MangaResponseConverter
) : RateResponseConverter {

    override fun apply(t: List<RateResponse?>): List<Rate> =
            t.mapNotNull { convertResponse(it) }.toList()

    override fun convertResponse(it: RateResponse?): Rate? {
        if (it == null) {
            return null
        }

        return Rate(
                it.id,
                it.score,
                it.status,
                it.text,
                it.textHtml,
                it.episodes,
                it.chapters,
                it.volumes,
                it.rewatches,
                animeConverter.convertResponse(it.anime),
                mangaConverter.convertResponse(it.manga)
        )
    }

    override fun convertUserRateResponse(targetId: Long?, it: UserRateResponse?): UserRate? {
        if (it == null) {
            return null
        }

        return UserRate(
                it.id,
                it.userId,
                it.targetId ?: targetId,
                it.targetType,
                it.score,
                it.status,
                it.rewatches,
                it.episodes,
                it.volumes,
                it.chapters,
                it.text,
                it.textHtml,
                it.dateCreated,
                it.dateUpdated
        )
    }

    override fun convertCreateOrUpdateRequest(targetId: Long, type: Type, rate: UserRate, userId: Long): UserRateCreateOrUpdateRequest =
            UserRateCreateOrUpdateRequest(UserRateResponse(
                    rate.id,
                    userId,
                    targetId,
                    type,
                    rate.score,
                    rate.status,
                    rate.rewatches,
                    rate.episodes,
                    rate.volumes,
                    rate.chapters,
                    rate.text,
                    rate.textHtml,
                    rate.dateCreated,
                    rate.dateUpdated
            ))

    override fun convertCreateOrUpdateRequest(rate: UserRate): UserRateCreateOrUpdateRequest =
            UserRateCreateOrUpdateRequest(UserRateResponse(
                    rate.id,
                    rate.userId,
                    rate.targetId,
                    rate.targetType,
                    rate.score,
                    rate.status,
                    rate.rewatches,
                    rate.episodes,
                    rate.volumes,
                    rate.chapters,
                    rate.text,
                    rate.textHtml,
                    rate.dateCreated,
                    rate.dateUpdated
            ))
}