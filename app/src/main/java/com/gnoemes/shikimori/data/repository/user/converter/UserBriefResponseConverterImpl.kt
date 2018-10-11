package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.entity.user.data.UserBriefResponse
import com.gnoemes.shikimori.entity.user.data.UserImageResponse
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.entity.user.domain.UserImage
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class UserBriefResponseConverterImpl @Inject constructor() : UserBriefResponseConverter {
    override fun apply(t: List<UserBriefResponse?>): List<UserBrief> =
            t.map { convertResponse(it)!! }

    override fun convertResponse(it: UserBriefResponse?): UserBrief? {
        if (it == null) {
            return null
        }

        return UserBrief(
                it.id,
                it.nickname,
                it.avatar?.appendHostIfNeed(),
                convertImage(it.image),
                it.dateLastOnline,
                it.name,
                it.sex,
                it.website,
                it.dateBirth,
                it.locale
        )
    }

    private fun convertImage(image: UserImageResponse): UserImage = UserImage(
            image.x160?.appendHostIfNeed(),
            image.x148?.appendHostIfNeed(),
            image.x80?.appendHostIfNeed(),
            image.x64?.appendHostIfNeed(),
            image.x48?.appendHostIfNeed(),
            image.x32?.appendHostIfNeed(),
            image.x16?.appendHostIfNeed()
    )
}