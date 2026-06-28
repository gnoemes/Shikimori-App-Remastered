package com.gnoemes.shikimori.data.repository.rates

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.ChapterDbSource
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.local.db.MangaRateSyncDbSource
import com.gnoemes.shikimori.data.network.UserApi
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.data.RateResponse
import com.gnoemes.shikimori.entity.rates.data.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.entity.rates.data.UserRateResponse
import com.gnoemes.shikimori.entity.rates.domain.Rate
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.NoSuchElementException

@DisplayName("RatesRepositoryImpl")
class RatesRepositoryImplTest {

    private val api: UserApi = mockk()
    private val converter: RateResponseConverter = mockk()
    private val episodeDbSource: EpisodeDbSource = mockk()
    private val chapterDbSource: ChapterDbSource = mockk()
    private val animeSyncSource: AnimeRateSyncDbSource = mockk()
    private val mangaSyncSource: MangaRateSyncDbSource = mockk()
    private lateinit var repository: RatesRepositoryImpl

    private val userId = 42L
    private val animeId = 123L
    private val mangaId = 456L
    private val rateId = 789L
    private val page = 1
    private val limit = 10

    @BeforeEach
    fun setup() {
        repository = RatesRepositoryImpl(
            api = api,
            converter = converter,
            episodeDbSource = episodeDbSource,
            chapterDbSource = chapterDbSource,
            animeSyncSource = animeSyncSource,
            mangaSyncSource = mangaSyncSource
        )
    }

    @Nested
    @DisplayName("getAnimeRates")
    inner class GetAnimeRates {

        @Test
        fun `should fetch anime rates from API and convert`() {
            val responseList = listOf<RateResponse>(mockk())
            val expectedRates = listOf(
                Rate(
                    id = 1L, score = 8, status = RateStatus.WATCHING,
                    text = null, textHtml = null, episodes = 5, chapters = null,
                    volumes = null, rewatches = 0, createdDateTime = null,
                    updatedDateTime = null, anime = null, manga = null
                )
            )

            every {
                api.getUserAnimeRates(userId, page, limit, RateStatus.WATCHING.status)
            } returns Single.just(responseList)
            every { converter.apply(responseList) } returns expectedRates

            val result = repository.getAnimeRates(userId, page, limit, RateStatus.WATCHING).blockingGet()

            assertEquals(expectedRates, result)
            verify { api.getUserAnimeRates(userId, page, limit, RateStatus.WATCHING.status) }
            verify { converter.apply(responseList) }
        }

        @Test
        fun `should return empty list on NoSuchElementException`() {
            every {
                api.getUserAnimeRates(userId, page, limit, RateStatus.COMPLETED.status)
            } returns Single.error(NoSuchElementException("Not found"))

            val result = repository.getAnimeRates(userId, page, limit, RateStatus.COMPLETED).blockingGet()

            assertTrue(result.isEmpty())
        }

        @Test
        fun `should propagate non-NoSuchElement errors`() {
            every {
                api.getUserAnimeRates(userId, page, limit, RateStatus.PLANNED.status)
            } returns Single.error(RuntimeException("Network error"))

            try {
                repository.getAnimeRates(userId, page, limit, RateStatus.PLANNED).blockingGet()
                assert(false) { "Expected exception" }
            } catch (e: RuntimeException) {
                assertEquals("Network error", e.message)
            }
        }
    }

    @Nested
    @DisplayName("getMangaRates")
    inner class GetMangaRates {

        @Test
        fun `should fetch manga rates from API and convert`() {
            val responseList = listOf<RateResponse>(mockk())
            val expectedRates = listOf(
                Rate(
                    id = 1L, score = 7, status = RateStatus.COMPLETED,
                    text = null, textHtml = null, episodes = null, chapters = 3,
                    volumes = null, rewatches = 0, createdDateTime = null,
                    updatedDateTime = null, anime = null, manga = null
                )
            )

            every {
                api.getUserMangaRates(userId, page, limit, RateStatus.COMPLETED.status)
            } returns Single.just(responseList)
            every { converter.apply(responseList) } returns expectedRates

            val result = repository.getMangaRates(userId, page, limit, RateStatus.COMPLETED).blockingGet()

            assertEquals(expectedRates, result)
            verify { api.getUserMangaRates(userId, page, limit, RateStatus.COMPLETED.status) }
            verify { converter.apply(responseList) }
        }

        @Test
        fun `should return empty list on NoSuchElementException`() {
            every {
                api.getUserMangaRates(userId, page, limit, RateStatus.COMPLETED.status)
            } returns Single.error(NoSuchElementException("Not found"))

            val result = repository.getMangaRates(userId, page, limit, RateStatus.COMPLETED).blockingGet()

            assertTrue(result.isEmpty())
        }
    }

    @Nested
    @DisplayName("getUserRates")
    inner class GetUserRates {

        @Test
        fun `should fetch user rates from API and convert each item`() {
            val responseList = listOf(
                UserRateResponse(id = 1L, userId = userId, targetId = animeId, targetType = Type.ANIME)
            )
            val expectedUserRates = listOf(
                UserRate(id = 1L, userId = userId, targetId = animeId, targetType = Type.ANIME)
            )

            every {
                api.getUserRates(userId, animeId, "Anime", null, page, limit)
            } returns Single.just(responseList)
            every { converter.convertUserRateResponse(animeId, responseList[0]) } returns expectedUserRates[0]

            val result = repository.getUserRates(userId, animeId, Type.ANIME, null, page, limit).blockingGet()

            assertEquals(expectedUserRates, result)
            verify { api.getUserRates(userId, animeId, "Anime", null, page, limit) }
            verify { converter.convertUserRateResponse(animeId, responseList[0]) }
        }
    }

    @Nested
    @DisplayName("getRate")
    inner class GetRate {

        @Test
        fun `should fetch rate from API, convert, and sync`() {
            val response = UserRateResponse(id = rateId, userId = userId, targetId = animeId, targetType = Type.ANIME)
            val userRate = UserRate(id = rateId, userId = userId, targetId = animeId, targetType = Type.ANIME, episodes = 5)

            every { api.getRate(rateId) } returns Single.just(response)
            every { converter.convertUserRateResponse(null, response) } returns userRate
            every { animeSyncSource.saveRate(userRate) } returns Completable.complete()

            val result = repository.getRate(rateId).blockingGet()

            assertEquals(userRate, result)
            verify { api.getRate(rateId) }
            verify { converter.convertUserRateResponse(null, response) }
            verify { animeSyncSource.saveRate(userRate) }
        }
    }

    @Nested
    @DisplayName("createRate")
    inner class CreateRate {

        @Test
        fun `should create anime rate using watched count from local`() {
            val userRate = UserRate(id = null, userId = userId, targetId = animeId, targetType = Type.ANIME, episodes = 0)
            val updatedRate = userRate.copy(episodes = 5)
            val createRequest = UserRateCreateOrUpdateRequest(UserRateResponse(targetId = animeId))
            val response = UserRateResponse(id = rateId, userId = userId, targetId = animeId, targetType = Type.ANIME, episodes = 5)
            val savedRate = UserRate(id = rateId, userId = userId, targetId = animeId, targetType = Type.ANIME, episodes = 5)

            every { episodeDbSource.getWatchedEpisodesCount(animeId) } returns Single.just(5)
            every { converter.convertCreateOrUpdateRequest(animeId, Type.ANIME, updatedRate, userId) } returns createRequest
            every { api.createRate(createRequest) } returns Single.just(response)
            every { converter.convertUserRateResponse(animeId, response) } returns savedRate
            every { animeSyncSource.saveRate(savedRate) } returns Completable.complete()

            repository.createRate(animeId, Type.ANIME, userRate, userId).blockingAwait()

            verify { episodeDbSource.getWatchedEpisodesCount(animeId) }
            verify { converter.convertCreateOrUpdateRequest(animeId, Type.ANIME, updatedRate, userId) }
            verify { api.createRate(createRequest) }
            verify { animeSyncSource.saveRate(savedRate) }
        }

        @Test
        fun `should create manga rate using chapter count from local`() {
            val userRate = UserRate(id = null, userId = userId, targetId = mangaId, targetType = Type.MANGA, chapters = 0)
            val updatedRate = userRate.copy(chapters = 3)
            val createRequest = UserRateCreateOrUpdateRequest(UserRateResponse(targetId = mangaId))
            val response = UserRateResponse(id = rateId, userId = userId, targetId = mangaId, targetType = Type.MANGA, chapters = 3)
            val savedRate = UserRate(id = rateId, userId = userId, targetId = mangaId, targetType = Type.MANGA, chapters = 3)

            every { chapterDbSource.getReadedChapterCount(mangaId) } returns Single.just(3)
            every { converter.convertCreateOrUpdateRequest(mangaId, Type.MANGA, updatedRate, userId) } returns createRequest
            every { api.createRate(createRequest) } returns Single.just(response)
            every { converter.convertUserRateResponse(mangaId, response) } returns savedRate
            every { mangaSyncSource.saveRate(savedRate) } returns Completable.complete()

            repository.createRate(mangaId, Type.MANGA, userRate, userId).blockingAwait()

            verify { chapterDbSource.getReadedChapterCount(mangaId) }
            verify { converter.convertCreateOrUpdateRequest(mangaId, Type.MANGA, updatedRate, userId) }
            verify { api.createRate(createRequest) }
            verify { mangaSyncSource.saveRate(savedRate) }
        }

        @Test
        fun `should error on unknown type`() {
            val userRate = UserRate()

            try {
                repository.createRate(animeId, Type.UNKNOWN, userRate, userId).blockingAwait()
                assert(false) { "Expected IllegalStateException" }
            } catch (e: IllegalStateException) {
                // expected
            }
        }
    }

    @Nested
    @DisplayName("createRateWithResult")
    inner class CreateRateWithResult {

        @Test
        fun `should create anime rate and return UserRate`() {
            val userRate = UserRate(id = null, userId = userId, targetId = animeId, targetType = Type.ANIME, episodes = 0)
            val updatedRate = userRate.copy(episodes = 5)
            val createRequest = UserRateCreateOrUpdateRequest(UserRateResponse(targetId = animeId))
            val response = UserRateResponse(id = rateId, userId = userId, targetId = animeId, targetType = Type.ANIME, episodes = 5)
            val savedRate = UserRate(id = rateId, userId = userId, targetId = animeId, targetType = Type.ANIME, episodes = 5)

            every { episodeDbSource.getWatchedEpisodesCount(animeId) } returns Single.just(5)
            every { converter.convertCreateOrUpdateRequest(animeId, Type.ANIME, updatedRate, userId) } returns createRequest
            every { api.createRate(createRequest) } returns Single.just(response)
            every { converter.convertUserRateResponse(animeId, response) } returns savedRate
            every { animeSyncSource.saveRate(savedRate) } returns Completable.complete()

            val result = repository.createRateWithResult(animeId, Type.ANIME, userRate, userId).blockingGet()

            assertEquals(savedRate, result)
        }
    }

    @Nested
    @DisplayName("syncRate")
    inner class SyncRate {

        @Test
        fun `syncRate by id should fetch rate then sync it`() {
            val userRate = UserRate(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 5)

            every { api.getRate(rateId) } returns Single.just(UserRateResponse(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 5))
            every { converter.convertUserRateResponse(null, any()) } returns userRate
            every { animeSyncSource.saveRate(userRate) } returns Completable.complete()

            repository.syncRate(rateId).blockingAwait()

            verify { api.getRate(rateId) }
            verify { animeSyncSource.saveRate(userRate) }
        }

        @Test
        fun `syncRate by UserRate should save anime rate via animeSyncSource`() {
            val userRate = UserRate(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 5)

            every { animeSyncSource.saveRate(userRate) } returns Completable.complete()

            repository.syncRate(userRate).blockingAwait()

            verify { animeSyncSource.saveRate(userRate) }
        }

        @Test
        fun `syncRate by UserRate should save manga rate via mangaSyncSource`() {
            val userRate = UserRate(id = rateId, targetId = mangaId, targetType = Type.MANGA, chapters = 3)

            every { mangaSyncSource.saveRate(userRate) } returns Completable.complete()

            repository.syncRate(userRate).blockingAwait()

            verify { mangaSyncSource.saveRate(userRate) }
        }
    }

    @Nested
    @DisplayName("updateRate")
    inner class UpdateRate {

        @Test
        fun `should update rate via API, convert response, and sync`() {
            val userRate = UserRate(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 5)
            val request = UserRateCreateOrUpdateRequest(UserRateResponse(targetId = animeId))
            val response = UserRateResponse(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 6)
            val updatedRate = UserRate(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 6)

            every { converter.convertCreateOrUpdateRequest(userRate) } returns request
            every { api.updateRate(rateId, request) } returns Single.just(response)
            every { converter.convertUserRateResponse(null, response) } returns updatedRate
            every { animeSyncSource.saveRate(updatedRate) } returns Completable.complete()

            repository.updateRate(userRate).blockingAwait()

            verify { converter.convertCreateOrUpdateRequest(userRate) }
            verify { api.updateRate(rateId, request) }
            verify { animeSyncSource.saveRate(updatedRate) }
        }
    }

    @Nested
    @DisplayName("deleteRate")
    inner class DeleteRate {

        @Test
        fun `should fetch rate, delete local data, then delete via API`() {
            val userRate = UserRate(id = rateId, targetId = animeId, targetType = Type.ANIME)

            every { api.getRate(rateId) } returns Single.just(UserRateResponse(id = rateId, targetId = animeId, targetType = Type.ANIME))
            every { converter.convertUserRateResponse(null, any()) } returns userRate
            every { episodeDbSource.clearEpisodes(animeId) } returns Completable.complete()
            every { animeSyncSource.clearRate(animeId) } returns Completable.complete()
            every { api.deleteRate(rateId) } returns Completable.complete()

            repository.deleteRate(rateId).blockingAwait()

            verify { api.getRate(rateId) }
            verify { episodeDbSource.clearEpisodes(animeId) }
            verify { animeSyncSource.clearRate(animeId) }
            verify { api.deleteRate(rateId) }
        }

        @Test
        fun `should delete manga rate with chapter cleanup`() {
            val userRate = UserRate(id = rateId, targetId = mangaId, targetType = Type.MANGA)

            every { api.getRate(rateId) } returns Single.just(UserRateResponse(id = rateId, targetId = mangaId, targetType = Type.MANGA))
            every { converter.convertUserRateResponse(null, any()) } returns userRate
            every { chapterDbSource.clearChapters(mangaId) } returns Completable.complete()
            every { mangaSyncSource.clearRate(mangaId) } returns Completable.complete()
            every { api.deleteRate(rateId) } returns Completable.complete()

            repository.deleteRate(rateId).blockingAwait()

            verify { chapterDbSource.clearChapters(mangaId) }
            verify { mangaSyncSource.clearRate(mangaId) }
            verify { api.deleteRate(rateId) }
        }
    }

    @Nested
    @DisplayName("increment")
    inner class Increment {

        @Test
        fun `increment by rateId should directly call API`() {
            every { api.increment(rateId) } returns Completable.complete()

            repository.increment(rateId).blockingAwait()

            verify { api.increment(rateId) }
        }

        @Test
        fun `increment anime rate should sync episode count then call API`() {
            val userRate = UserRate(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 5)
            val syncedRate = UserRate(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 5)

            every { episodeDbSource.getWatchedEpisodesCount(animeId) } returns Single.just(5)
            every { animeSyncSource.getRate(rateId) } returns Single.just(syncedRate)
            every { animeSyncSource.saveRate(syncedRate.copy(episodes = 5)) } returns Completable.complete()
            every { api.increment(rateId) } returns Completable.complete()

            repository.increment(userRate).blockingAwait()

            verify { episodeDbSource.getWatchedEpisodesCount(animeId) }
            verify { animeSyncSource.getRate(rateId) }
            verify { animeSyncSource.saveRate(syncedRate.copy(episodes = 5)) }
            verify { api.increment(rateId) }
        }

        @Test
        fun `increment anime rate should fallback to network when local getRate fails`() {
            val userRate = UserRate(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 5)
            val networkRate = UserRate(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 3)

            every { episodeDbSource.getWatchedEpisodesCount(animeId) } returns Single.just(5)
            every { animeSyncSource.getRate(rateId) } returns Single.error(RuntimeException("Local not found"))
            every { api.getRate(rateId) } returns Single.just(UserRateResponse(id = rateId, targetId = animeId, targetType = Type.ANIME, episodes = 3))
            every { converter.convertUserRateResponse(null, any()) } returns networkRate
            every { animeSyncSource.saveRate(networkRate.copy(episodes = 5)) } returns Completable.complete()
            every { api.increment(rateId) } returns Completable.complete()

            repository.increment(userRate).blockingAwait()

            verify { api.getRate(rateId) }
            verify { animeSyncSource.saveRate(networkRate.copy(episodes = 5)) }
            verify { api.increment(rateId) }
        }

        @Test
        fun `increment manga rate should sync chapter count then call API`() {
            val userRate = UserRate(id = rateId, targetId = mangaId, targetType = Type.MANGA, chapters = 3)

            every { chapterDbSource.getReadedChapterCount(mangaId) } returns Single.just(3)
            every { mangaSyncSource.getRate(rateId) } returns Single.just(userRate)
            every { mangaSyncSource.saveRate(userRate.copy(chapters = 4)) } returns Completable.complete()
            every { api.increment(rateId) } returns Completable.complete()

            repository.increment(userRate).blockingAwait()

            verify { chapterDbSource.getReadedChapterCount(mangaId) }
            verify { mangaSyncSource.getRate(rateId) }
            verify { mangaSyncSource.saveRate(userRate.copy(chapters = 4)) }
            verify { api.increment(rateId) }
        }

        @Test
        fun `increment on unknown type should error`() {
            val userRate = UserRate(id = rateId, targetType = Type.UNKNOWN)

            try {
                repository.increment(userRate).blockingAwait()
                assert(false) { "Expected IllegalArgumentException" }
            } catch (e: IllegalArgumentException) {
                // expected
            }
        }
    }
}
