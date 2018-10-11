package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.entity.user.data.StatusResponse
import com.gnoemes.shikimori.entity.user.data.UserDetailsResponse
import com.gnoemes.shikimori.entity.user.data.UserImageResponse
import com.gnoemes.shikimori.entity.user.data.UserStatsResponse
import com.gnoemes.shikimori.entity.user.domain.Status
import com.gnoemes.shikimori.entity.user.domain.UserDetails
import com.gnoemes.shikimori.entity.user.domain.UserImage
import com.gnoemes.shikimori.entity.user.domain.UserStats
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class UserDetailsResponseConverterImpl @Inject constructor() : UserDetailsResponseConverter {

    override fun convertResponse(it: UserDetailsResponse): UserDetails = UserDetails(
            it.id,
            it.nickname,
            convertImage(it.image),
            it.dateLastOnline,
            it.name,
            it.sex,
            it.website,
            it.dateBirth,
            it.locale,
            it.fullYears,
            it.lastOnline,
            it.location,
            it.isBanned,
            it.about,
            convertCommonInfo(it.commonInfo),
            it.isShowComments,
            it.isFriend,
            it.isIgnored,
            convertStats(it.stats)
    )

    private fun convertCommonInfo(commonInfo: List<String>): String {
        val DELIMITER = " / "
        val builder = StringBuilder()
        commonInfo.forEach { builder.append(it).append(DELIMITER) }

        builder.replace(builder.lastIndexOf(DELIMITER), builder.length - 1, "")
        return builder.toString()
                .replace("<.[spn].+?>", "")
                .replace("(class.\".+?\" )", "")

    }

    private fun convertStats(stats: UserStatsResponse): UserStats {
        val animes = stats.status.anime.map { convertStatus(it) }
        val mangas = stats.status.manga.map { convertStatus(it) }

        return UserStats(animes, mangas, stats.hasAnime, stats.hasManga)
    }

    private fun convertStatus(it: StatusResponse): Status =
            Status(it.id, it.name, it.size, it.type)

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