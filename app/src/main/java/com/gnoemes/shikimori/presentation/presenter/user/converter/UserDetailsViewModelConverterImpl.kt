package com.gnoemes.shikimori.presentation.presenter.user.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.club.domain.Club
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.user.domain.*
import com.gnoemes.shikimori.entity.user.presentation.*
import java.math.MathContext
import java.math.RoundingMode
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

    override fun convertAnimeRate(stats: UserStats): UserRateViewModel = UserRateViewModel(
            true,
            convertRate(stats.animeStatuses),
            countAvgScore(stats.scores.anime),
            convertScores(stats.scores.anime),
            convertTypes(true, stats.types.anime),
            convertRatings(stats.ratings.anime)
    )

    override fun convertMangaRate(stats: UserStats): UserRateViewModel = UserRateViewModel(
            false,
            convertRate(stats.mangaStatuses),
            countAvgScore(stats.scores.manga ?: emptyList()),
            convertScores(stats.scores.manga ?: emptyList()),
            convertTypes(false, stats.types.manga ?: emptyList()),
            emptyList()
    )

    private fun countAvgScore(scores: List<Statistic>): Float {
        if (scores.isEmpty()) return 0f

        val sum = scores.sumBy { it.value }
        return scores
                .sumBy { it.name.toInt() * it.value }
                .toFloat()
                .div(sum)
                .toBigDecimal(MathContext(3, RoundingMode.UP))
                .toFloat()
    }

    private fun convertRatings(ratings: List<Statistic>): List<UserStatisticItem> {
        val items = mutableListOf(
                UserStatisticItem("G", 0, 0f),
                UserStatisticItem("PG", 0, 0f),
                UserStatisticItem("PG-13", 0, 0f),
                UserStatisticItem("R-17", 0, 0f),
                UserStatisticItem("R+", 0, 0f),
                UserStatisticItem("Rx", 0, 0f)
        )
        val sum = ratings.sumBy { it.value }

        return items
                .map { stat ->
                    val item = ratings.find { stat.category.equals(it.name) }
                    if (item != null) stat.copy(count = item.value, progress = item.value / sum.toFloat())
                    else stat
                }
    }

    private fun convertTypes(isAnime: Boolean, types: List<Statistic>): List<UserStatisticItem> {
        val items = if (isAnime) mutableListOf(
                Pair("TV Series|Сериал", UserStatisticItem(context.getString(R.string.type_tv_long_translatable), 0, 0f)),
                Pair("Movie|Фильм", UserStatisticItem(context.getString(R.string.type_movie_translatable), 0, 0f)),
                Pair("OVA", UserStatisticItem(context.getString(R.string.type_ova), 0, 0f)),
                Pair("ONA", UserStatisticItem(context.getString(R.string.type_ona), 0, 0f)),
                Pair("Special|Спешл", UserStatisticItem(context.getString(R.string.type_special_translatable), 0, 0f)),
                Pair("Music|Клип", UserStatisticItem(context.getString(R.string.type_music_translatable), 0, 0f))
        ) else mutableListOf(
                Pair("Manga|Манга", UserStatisticItem(context.getString(R.string.type_manga_translatable), 0, 0f)),
                Pair("Manhwa|Манхва", UserStatisticItem(context.getString(R.string.type_manhwa_translatable), 0, 0f)),
                Pair("Manhua|Маньхуа", UserStatisticItem(context.getString(R.string.type_manhua_translatable), 0, 0f)),
                Pair("Light Novel|Ранобэ", UserStatisticItem(context.getString(R.string.type_novel_translatable), 0, 0f)),
                Pair("One Shot|Ваншот", UserStatisticItem(context.getString(R.string.type_one_shot_translatable), 0, 0f)),
                Pair("Doujin|Додзинси", UserStatisticItem(context.getString(R.string.type_doujin_translatable), 0, 0f))
        )

        val sum = types.sumBy { it.value }

        return items
                .map { pair ->
                    val item = types.find { pair.first.split("|").contains(it.name) }
                    if (item != null) pair.second.copy(count = item.value, progress = item.value / sum.toFloat())
                    else pair.second
                }
    }


    private fun convertScores(scores: List<Statistic>): List<UserStatisticItem> {
        val allScores = (1..10).toMutableList()
        val sum = scores.sumBy { it.value }

        return scores
                .asSequence()
                .map {
                    val score = it.name.toIntOrNull() ?: 0
                    if (allScores.contains(score)) {
                        allScores.remove(score)
                        UserStatisticItem(it.name, it.value, it.value / sum.toFloat())
                    } else null
                }
                .filterNotNull()
                .toMutableList()
                .union(allScores.map { UserStatisticItem(it.toString(), 0, 0f) })
                .sortedByDescending { it.category.toInt() }
                .toMutableList()
    }

    private fun convertRate(statuses: List<Status>?): Map<RateProgressStatus, Int> {
        if (statuses.isNullOrEmpty()) return emptyMap()

        return mapOf(
                Pair(RateProgressStatus.PLANNED, statuses.countRate { it.name == RateStatus.PLANNED }),
                Pair(RateProgressStatus.COMPLETED, statuses.countRate { it.name == RateStatus.COMPLETED }),
                Pair(RateProgressStatus.IN_PROGRESS, statuses.countRate { it.name == RateStatus.WATCHING }),
                Pair(RateProgressStatus.DROPPED, statuses.countRate { it.name == RateStatus.DROPPED })
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