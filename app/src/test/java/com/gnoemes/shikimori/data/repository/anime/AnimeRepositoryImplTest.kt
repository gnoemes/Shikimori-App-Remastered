package com.gnoemes.shikimori.data.repository.anime

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.network.AnimeApi
import com.gnoemes.shikimori.data.repository.anime.converter.AnimeDetailsResponseConverter
import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter
import com.gnoemes.shikimori.data.repository.common.FranchiseResponseConverter
import com.gnoemes.shikimori.data.repository.common.LinkResponseConverter
import com.gnoemes.shikimori.data.repository.common.RolesResponseConverter
import com.gnoemes.shikimori.entity.anime.data.AnimeDetailsResponse
import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.anime.data.ScreenshotResponse
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.anime.domain.Screenshot
import com.gnoemes.shikimori.entity.common.data.FranchiseResponse
import com.gnoemes.shikimori.entity.common.data.LinkResponse
import com.gnoemes.shikimori.entity.common.data.RolesResponse
import com.gnoemes.shikimori.entity.common.domain.Franchise
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.domain.Roles
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("AnimeRepositoryImpl")
class AnimeRepositoryImplTest {

    private val api: AnimeApi = mockk()
    private val syncDbSource: AnimeRateSyncDbSource = mockk()
    private val episodeDbSource: EpisodeDbSource = mockk()
    private val linkConverter: LinkResponseConverter = mockk()
    private val animeConverter: AnimeResponseConverter = mockk()
    private val franchiseConverter: FranchiseResponseConverter = mockk()
    private val detailsConverter: AnimeDetailsResponseConverter = mockk()
    private val rolesConverter: RolesResponseConverter = mockk()
    private lateinit var repository: AnimeRepositoryImpl

    private val animeId = 123L

    @BeforeEach
    fun setup() {
        repository = AnimeRepositoryImpl(
            api = api,
            syncDbSource = syncDbSource,
            episodeDbSource = episodeDbSource,
            linkConverter = linkConverter,
            animeConverter = animeConverter,
            franchiseConverter = franchiseConverter,
            detailsConverter = detailsConverter,
            rolesConverter = rolesConverter
        )
    }

    @Nested
    @DisplayName("getDetails")
    inner class GetDetails {

        @Test
        fun `should fetch from API, convert, and sync rate`() {
            val response = AnimeDetailsResponse(
                id = animeId, name = "Test Anime", nameRu = null,
                image = null, url = null, _type = AnimeType.TV, _status = Status.RELEASED,
                episodes = 12, episodesAired = 12, dateAired = null,
                nextEpisodeDate = null, dateReleased = null, namesEnglish = null,
                namesJapanese = null, ageRating = null, score = "7.5", duration = 24,
                description = null, descriptionHtml = null, franchise = null,
                favoured = false, topicId = null, genres = null, userRate = null,
                videoResponses = null, studioResponses = null,
                rateScoresStats = null, rateStatusesStats = null
            )
            val expectedDetails = AnimeDetails(
                id = animeId, name = "Test Anime", nameRu = null,
                image = Image(original = null, preview = null, x96 = null, x48 = null),
                url = "", type = AnimeType.TV,
                status = Status.RELEASED, episodes = 12, episodesAired = 12,
                dateAired = null, dateReleased = null, nextEpisodeDate = null,
                namesEnglish = null, namesJapanese = null, ageRating = null,
                score = 7.5, duration = 24, description = null,
                descriptionHtml = "", franchise = null, favoured = false,
                topicId = null, genres = emptyList(), userRate = null,
                videos = emptyList(), studios = emptyList(),
                rateScoresStats = emptyList(), rateStatusesStats = emptyList()
            )

            every { api.getDetails(animeId) } returns Single.just(response)
            every { detailsConverter.apply(response) } returns expectedDetails

            val result = repository.getDetails(animeId).blockingGet()

            assertEquals(expectedDetails, result)
            verify { api.getDetails(animeId) }
            verify { detailsConverter.apply(response) }
        }

        @Test
        fun `should sync userRate when details contains valid rate`() {
            val userRate = UserRate(
                id = 1L, targetId = animeId, episodes = 5,
                status = RateStatus.WATCHING, score = 8.0
            )
            val response = AnimeDetailsResponse(
                id = animeId, name = "Anime", nameRu = null,
                image = null, url = null, _type = AnimeType.TV, _status = Status.RELEASED,
                episodes = 12, episodesAired = 12, dateAired = null,
                nextEpisodeDate = null, dateReleased = null, namesEnglish = null,
                namesJapanese = null, ageRating = null, score = "8.0", duration = 24,
                description = null, descriptionHtml = null, franchise = null,
                favoured = false, topicId = null, genres = null, userRate = null,
                videoResponses = null, studioResponses = null,
                rateScoresStats = null, rateStatusesStats = null
            )
            val detailsWithRate = AnimeDetails(
                id = animeId, name = "Anime", nameRu = null,
                image = Image(null, null, null, null),
                url = "", type = AnimeType.TV,
                status = Status.RELEASED, episodes = 12, episodesAired = 12,
                dateAired = null, dateReleased = null, nextEpisodeDate = null,
                namesEnglish = null, namesJapanese = null, ageRating = null,
                score = 8.0, duration = 24, description = null,
                descriptionHtml = "", franchise = null, favoured = false,
                topicId = null, genres = emptyList(), userRate = userRate,
                videos = emptyList(), studios = emptyList(),
                rateScoresStats = emptyList(), rateStatusesStats = emptyList()
            )

            every { api.getDetails(animeId) } returns Single.just(response)
            every { detailsConverter.apply(response) } returns detailsWithRate
            every { syncDbSource.saveRate(userRate) } returns Completable.complete()

            val result = repository.getDetails(animeId).blockingGet()

            assertEquals(detailsWithRate, result)
            verify { syncDbSource.saveRate(userRate) }
        }

        @Test
        fun `should still return details when syncRate fails`() {
            val userRate = UserRate(
                id = 1L, targetId = animeId, episodes = 5,
                status = RateStatus.WATCHING
            )
            val response = AnimeDetailsResponse(
                id = animeId, name = "Anime", nameRu = null,
                image = null, url = null, _type = AnimeType.TV, _status = Status.RELEASED,
                episodes = 12, episodesAired = 12, dateAired = null,
                nextEpisodeDate = null, dateReleased = null, namesEnglish = null,
                namesJapanese = null, ageRating = null, score = "8.0", duration = 24,
                description = null, descriptionHtml = null, franchise = null,
                favoured = false, topicId = null, genres = null, userRate = null,
                videoResponses = null, studioResponses = null,
                rateScoresStats = null, rateStatusesStats = null
            )
            val detailsWithRate = AnimeDetails(
                id = animeId, name = "Anime", nameRu = null,
                image = Image(null, null, null, null),
                url = "", type = AnimeType.TV,
                status = Status.RELEASED, episodes = 12, episodesAired = 12,
                dateAired = null, dateReleased = null, nextEpisodeDate = null,
                namesEnglish = null, namesJapanese = null, ageRating = null,
                score = 8.0, duration = 24, description = null,
                descriptionHtml = "", franchise = null, favoured = false,
                topicId = null, genres = emptyList(), userRate = userRate,
                videos = emptyList(), studios = emptyList(),
                rateScoresStats = emptyList(), rateStatusesStats = emptyList()
            )

            every { api.getDetails(animeId) } returns Single.just(response)
            every { detailsConverter.apply(response) } returns detailsWithRate
            every { syncDbSource.saveRate(userRate) } returns Completable.error(RuntimeException("DB error"))

            val result = repository.getDetails(animeId).blockingGet()

            assertEquals(detailsWithRate, result)
        }
    }

    @Nested
    @DisplayName("getRoles")
    inner class GetRoles {

        @Test
        fun `should fetch roles from API and convert`() {
            val responseList = listOf<RolesResponse>(mockk())
            val expectedRoles = mockk<Roles>()

            every { api.getRoles(animeId) } returns Single.just(responseList)
            every { rolesConverter.apply(responseList) } returns expectedRoles

            val result = repository.getRoles(animeId).blockingGet()

            assertEquals(expectedRoles, result)
            verify { api.getRoles(animeId) }
            verify { rolesConverter.apply(responseList) }
        }
    }

    @Nested
    @DisplayName("getLinks")
    inner class GetLinks {

        @Test
        fun `should fetch links from API and convert`() {
            val responseList = listOf(LinkResponse(id = 1L, name = "Wiki", url = "https://wiki.example.com"))
            val expectedLinks = listOf(Link(id = 1L, name = "Wiki", url = "https://wiki.example.com"))

            every { api.getLinks(animeId) } returns Single.just(responseList)
            every { linkConverter.apply(responseList) } returns expectedLinks

            val result = repository.getLinks(animeId).blockingGet()

            assertEquals(expectedLinks, result)
            verify { api.getLinks(animeId) }
            verify { linkConverter.apply(responseList) }
        }
    }

    @Nested
    @DisplayName("getSimilar")
    inner class GetSimilar {

        @Test
        fun `should fetch similar anime from API and convert`() {
            val responseList = listOf<AnimeResponse>(mockk())
            val expectedAnime = listOf(
                Anime(
                    id = 1L, name = "Similar Anime", nameRu = null,
                    image = Image(null, null, null, null),
                    url = "", type = AnimeType.TV,
                    score = 7.0, status = Status.RELEASED, episodes = 12,
                    episodesAired = 12, dateAired = null, dateReleased = null
                )
            )

            every { api.getSimilar(animeId) } returns Single.just(responseList)
            every { animeConverter.apply(responseList) } returns expectedAnime

            val result = repository.getSimilar(animeId).blockingGet()

            assertEquals(expectedAnime, result)
            verify { api.getSimilar(animeId) }
            verify { animeConverter.apply(responseList) }
        }
    }

    @Nested
    @DisplayName("getFranchise")
    inner class GetFranchise {

        @Test
        fun `should fetch franchise from API and convert`() {
            val response = mockk<FranchiseResponse>()
            val expectedFranchise = mockk<Franchise>()

            every { api.getFranchise(animeId) } returns Single.just(response)
            every { franchiseConverter.apply(response) } returns expectedFranchise

            val result = repository.getFranchise(animeId).blockingGet()

            assertEquals(expectedFranchise, result)
            verify { api.getFranchise(animeId) }
            verify { franchiseConverter.apply(response) }
        }
    }

    @Nested
    @DisplayName("getScreenshots")
    inner class GetScreenshots {

        @Test
        fun `should fetch screenshots from API and map to domain`() {
            val responseList = listOf(
                ScreenshotResponse(original = "https://orig.example.com/1.jpg", preview = "https://prev.example.com/1.jpg"),
                ScreenshotResponse(original = "https://orig.example.com/2.jpg", preview = null)
            )

            every { api.getScreenshots(animeId) } returns Single.just(responseList)

            val result = repository.getScreenshots(animeId).blockingGet()

            assertEquals(2, result.size)
            assertEquals("https://orig.example.com/1.jpg", result[0].original)
            assertEquals("https://prev.example.com/1.jpg", result[0].preview)
            assertEquals("https://orig.example.com/2.jpg", result[1].original)
            assertEquals(null, result[1].preview)
            verify { api.getScreenshots(animeId) }
        }
    }

    @Nested
    @DisplayName("getLocalWatchedAnimeIds")
    inner class GetLocalWatchedAnimeIds {

        @Test
        fun `should get watched anime IDs from local db`() {
            val watchedIds = listOf(1L, 2L, 3L)

            every { episodeDbSource.getWatchedAnimeIds() } returns Single.just(watchedIds)

            val result = repository.getLocalWatchedAnimeIds().blockingGet()

            assertEquals(LinkedHashSet(watchedIds), result)
            verify { episodeDbSource.getWatchedAnimeIds() }
        }
    }
}
