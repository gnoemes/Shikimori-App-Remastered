package com.gnoemes.shikimori.data.repository.roles.converter

import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter
import com.gnoemes.shikimori.data.repository.common.CharacterResponseConverter
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.data.repository.common.MangaResponseConverter
import com.gnoemes.shikimori.entity.roles.data.PersonDetailsResponse
import com.gnoemes.shikimori.entity.roles.data.SeyuRoleResponse
import com.gnoemes.shikimori.entity.roles.data.WorkResponse
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.PersonDetails
import com.gnoemes.shikimori.entity.roles.domain.PersonType
import com.gnoemes.shikimori.entity.roles.domain.Work
import com.gnoemes.shikimori.utils.appendHostIfNeed
import org.joda.time.DateTime
import javax.inject.Inject

class PersonDetailsResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter,
        private val animeConverter: AnimeResponseConverter,
        private val mangaConverter: MangaResponseConverter,
        private val characterConverter: CharacterResponseConverter
) : PersonDetailsResponseConverter {

    override fun convertResponse(it: PersonDetailsResponse): PersonDetails = PersonDetails(
            it.id,
            it.name,
            it.nameRu,
            it.nameJp,
            imageConverter.convertResponse(it.image),
            it.url.appendHostIfNeed(),
            it.jobTitle,
            if (it.birthDay.year != null && it.birthDay.month != null && it.birthDay.day != null)
                DateTime(it.birthDay.year.toInt(), it.birthDay.month.toInt(), it.birthDay.day.toInt(), 0, 0)
            else null,
            convertWorks(it.works),
            convertCharacters(it.roles),
            it.rolesGrouped,
            it.topicId,
            convertPersonType(it.isProducer, it.isMangaka, it.isSeyu),
            convertPersonFavoriteType(it.isFavoritePerson, it.isFavoriteProducer, it.isFavoriteMangaka, it.isFavoriteSeyu)
    )

    private fun convertPersonFavoriteType(favoritePerson: Boolean, favoriteProducer: Boolean, favoriteMangaka: Boolean, favoriteSeyu: Boolean): PersonType {
        return when {
            favoritePerson -> PersonType.PERSON
            favoriteProducer -> PersonType.PRODUCER
            favoriteMangaka -> PersonType.MANGAKA
            favoriteSeyu -> PersonType.SEYU
            else -> PersonType.NONE
        }
    }

    private fun convertPersonType(producer: Boolean, mangaka: Boolean, seyu: Boolean): PersonType {
        return when {
            producer -> PersonType.PRODUCER
            mangaka -> PersonType.MANGAKA
            seyu -> PersonType.SEYU
            else -> PersonType.NONE
        }
    }

    private fun convertCharacters(roles: List<SeyuRoleResponse>?): List<Character> =
            roles?.map { it.characters }?.flatMap { characterConverter.apply(it) } ?: emptyList()

    private fun convertWorks(works: List<WorkResponse>?): List<Work> =
            works?.map {
                Work(
                        animeConverter.convertResponse(it.anime),
                        mangaConverter.convertResponse(it.manga),
                        it.role
                )
            } ?: emptyList()
}