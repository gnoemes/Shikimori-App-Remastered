package com.gnoemes.shikimori.presentation.presenter.user.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.club.domain.Club
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.user.domain.*
import com.gnoemes.shikimori.entity.user.presentation.*
import javax.inject.Inject

class UserDetailsViewModelConverterImpl @Inject constructor(
        private val context: Context
) : UserDetailsViewModelConverter {

    companion object {
        const val FRIENDS_INITIAL = 5
        const val CLUBS_INITIAL = 5
        const val FAVORITES_INITIAL = 3
    }

    override fun convertHead(t: UserDetails): UserHeadViewModel =
            UserHeadViewModel(
                    t.nickname,
                    t.lastOnline,
                    t.image
            )

    override fun convertInfo(t: UserDetails): UserInfoViewModel =
            UserInfoViewModel(
                    t.commonInfo,
                    t.isFriend,
                    t.isIgnored,
                    t.isMe
            )

    override fun convertFriends(t: List<UserBrief>): UserContentViewModel {
        val friends = t.map { UserContentItem(it.id, Type.USER, it.image.x160) }
        return convertContentViewModel(UserContentType.FRIENDS, friends)
    }

    override fun convertFavorites(t: FavoriteList): UserContentViewModel {
        val items = t
                .all
                .map { UserContentItem(it.id, convertFavoriteType(it.type), it.image) }
        return convertContentViewModel(UserContentType.FAVORITES, items)
    }

    override fun convertClubs(t: List<Club>): UserContentViewModel {
        val clubs = t.map { UserContentItem(it.id, it.linkedType, it.image.original) }
        return convertContentViewModel(UserContentType.CLUBS, clubs)
    }

    override fun convertAnimeRate(statuses: List<Status>?): UserRateViewModel = convertRate(true, statuses)

    override fun convertMangaRate(statuses: List<Status>?): UserRateViewModel = convertRate(false, statuses)

    private fun convertRate(isAnime: Boolean, statuses: List<Status>?): UserRateViewModel {
        if (statuses.isNullOrEmpty()) return UserRateViewModel(isAnime, emptyMap(), emptyMap())

        val inProgressStatuses = mutableListOf(RateStatus.PLANNED, RateStatus.WATCHING, RateStatus.ON_HOLD)

        val rates = mapOf(
                Pair(RateProgressStatus.COMPLETED, statuses.countRate { it.name == RateStatus.COMPLETED }),
                Pair(RateProgressStatus.IN_PROGRESS, statuses.countRate { inProgressStatuses.contains(it.name) }),
                Pair(RateProgressStatus.DROPPED, statuses.countRate { it.name == RateStatus.DROPPED })
        )

        val formatArray = if (isAnime) R.array.anime_rate_stasuses_with_count else R.array.manga_rate_stasuses_with_count

        val rateFormatStrings = RateStatus.values()
                .zip(context.resources.getStringArray(formatArray))
                .toMap()

        val rawRates = RateStatus.values()
                .associateBy({ it }, { rate -> statuses.countRate { it.name == rate } })
                .mapValues { String.format(rateFormatStrings.getValue(it.key), it.value) }

        return UserRateViewModel(
                isAnime,
                rates,
                rawRates
        )
    }

    private inline fun List<Status>.countRate(crossinline block: (Status) -> Boolean): Int = sumBy { if (block.invoke(it)) it.size else 0 }

    private fun convertContentViewModel(type: UserContentType, items: List<UserContentItem>): UserContentViewModel {
        val initialCount = getInitial(type)

        val isNeedMore = isNeedMoreItem(type, items.size)

        return UserContentViewModel(
                type,
                if (isNeedMore) items.take(initialCount) else items,
                isNeedMore,
                items.size - initialCount
        )
    }

    private fun getInitial(type: UserContentType): Int =
            when (type) {
                UserContentType.CLUBS -> CLUBS_INITIAL
                UserContentType.FAVORITES -> FAVORITES_INITIAL
                UserContentType.FRIENDS -> FRIENDS_INITIAL
            }

    private fun isNeedMoreItem(type: UserContentType, size: Int): Boolean =
            when (type) {
                UserContentType.FAVORITES -> size > 12
                else -> size > 15
            }

    private fun convertFavoriteType(type: FavoriteType): Type =
            when (type) {
                FavoriteType.ANIME -> Type.ANIME
                FavoriteType.CHARACTERS -> Type.CHARACTER
                FavoriteType.MANGA -> Type.MANGA
                FavoriteType.MANGAKAS -> Type.PERSON
                FavoriteType.PEOPLE -> Type.PERSON
                FavoriteType.PRODUCERS -> Type.PERSON
                FavoriteType.SEYU -> Type.PERSON
            }
}