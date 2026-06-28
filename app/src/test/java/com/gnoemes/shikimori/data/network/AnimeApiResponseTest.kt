package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.anime.data.AnimeDetailsResponse
import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.anime.data.AnimeVideoResponse
import com.gnoemes.shikimori.entity.anime.data.ScreenshotResponse
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.anime.domain.AnimeVideoType
import com.gnoemes.shikimori.entity.common.domain.AgeRating
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.reflect.Type
import java.util.Date

class AnimeApiResponseTest {

    private lateinit var gson: com.google.gson.Gson

    @BeforeEach
    fun setUp() {
        gson = GsonBuilder()
            .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer())
            .create()
    }

    /**
     * Inline DateTime deserializer matching the production DateTimeResponseConverterImpl logic.
     */
    private class DateTimeDeserializer : JsonDeserializer<DateTime> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DateTime {
            return try {
                DateTime(json.asString, DateTimeZone.UTC).withZone(DateTimeZone.getDefault())
            } catch (e: IllegalArgumentException) {
                val date: Date = context.deserialize(json, Date::class.java)
                DateTime(date)
            }
        }
    }

    @Test
    fun `deserialize anime response with all fields`() {
        val json = """
        {
            "id": 12345,
            "name": "Steins;Gate",
            "russian": "Штей;нс;Гейт",
            "image": {
                "original": "https://shikimori.one/assets/globals/missing.png",
                "preview": "https://shikimori.one/assets/globals/missing.png",
                "x96": "https://shikimori.one/assets/globals/missing.png",
                "x48": "https://shikimori.one/assets/globals/missing.png"
            },
            "url": "/animes/12345-steins-gate",
            "kind": "tv",
            "score": 9.07,
            "status": "released",
            "episodes": 24,
            "episodes_aired": 24,
            "aired_on": "2011-04-06",
            "released_on": "2011-09-14"
        }
        """.trimIndent()

        val anime = gson.fromJson(json, AnimeResponse::class.java)

        assertEquals(12345L, anime.id)
        assertEquals("Steins;Gate", anime.name)
        assertEquals("Штей;нс;Гейт", anime.nameRu)
        assertEquals("/animes/12345-steins-gate", anime.url)
        assertEquals(AnimeType.TV, anime.type)
        assertEquals(9.07, anime.score!!, 0.001)
        assertEquals(Status.RELEASED, anime.status)
        assertEquals(24, anime.episodes)
        assertEquals(24, anime.episodesAired)
        assertNotNull(anime.dateAired)
        assertNotNull(anime.dateReleased)

        // Image assertions
        assertNotNull(anime.image)
        assertNotNull(anime.image.original)
        assertNotNull(anime.image.preview)
        assertNotNull(anime.image.x96)
        assertNotNull(anime.image.x48)
    }

    @Test
    fun `deserialize anime response with minimal fields`() {
        val json = """
        {
            "id": 1,
            "name": "Cowboy Bebop",
            "russian": null,
            "image": {
                "original": null,
                "preview": null,
                "x96": null,
                "x48": null
            },
            "url": "/animes/1",
            "kind": null,
            "score": null,
            "status": null,
            "episodes": 26,
            "episodes_aired": 26,
            "aired_on": null,
            "released_on": null
        }
        """.trimIndent()

        val anime = gson.fromJson(json, AnimeResponse::class.java)

        assertEquals(1L, anime.id)
        assertEquals("Cowboy Bebop", anime.name)
        assertNull(anime.nameRu)
        assertEquals(AnimeType.NONE, anime.type)
        assertNull(anime.score)
        assertEquals(Status.NONE, anime.status)
        assertEquals(26, anime.episodes)
        assertEquals(26, anime.episodesAired)
        assertNull(anime.dateAired)
        assertNull(anime.dateReleased)
    }

    @Test
    fun `deserialize anime details response with all fields`() {
        val json = """
        {
            "id": 12345,
            "name": "Steins;Gate",
            "russian": "Штей;нс;Гейт",
            "image": {
                "original": "https://orig.png",
                "preview": "https://prev.png",
                "x96": "https://x96.png",
                "x48": "https://x48.png"
            },
            "url": "/animes/12345",
            "kind": "tv",
            "status": "released",
            "episodes": 24,
            "episodes_aired": 24,
            "aired_on": "2011-04-06",
            "next_episode_at": null,
            "released_on": "2011-09-14",
            "english": ["Steins;Gate"],
            "japanese": ["シュタインズ・ゲート"],
            "rating": "r",
            "score": "9.07",
            "duration": 24,
            "description": "A thrilling sci-fi story.",
            "description_html": "<p>A thrilling sci-fi story.</p>",
            "franchise": "steins_gate",
            "favoured": true,
            "topic_id": 123,
            "genres": [
                {"id": 1, "name": "Sci-Fi", "russian": "Научная фантастика", "kind": "genre"},
                {"id": 2, "name": "Thriller", "russian": null, "kind": "genre"}
            ],
            "user_rate": {
                "id": 456,
                "user_id": 789,
                "target_id": 12345,
                "target_type": "Anime",
                "score": 9.0,
                "status": "completed",
                "rewatches": 2,
                "episodes": 24,
                "volumes": null,
                "chapters": null,
                "text": "Great show!",
                "text_html": "<p>Great show!</p>",
                "created_at": "2024-01-15T10:30:00.000+03:00",
                "updated_at": "2024-01-20T12:00:00.000+03:00"
            },
            "videos": [
                {
                    "id": 1,
                    "name": "Opening 1",
                    "url": "https://video.example.com/op1",
                    "image_url": "https://img.example.com/op1.jpg",
                    "kind": "op",
                    "hosting": "youtube"
                },
                {
                    "id": 2,
                    "name": null,
                    "url": "https://video.example.com/ed1",
                    "image_url": null,
                    "kind": "ed",
                    "hosting": "vk"
                }
            ],
            "studios": [
                {"id": 1, "name": "White Fox", "filtered_name": "WhiteFox", "real": true, "image": null}
            ],
            "rates_scores_stats": [
                {"name": "9", "value": 100},
                {"name": "8", "value": 200}
            ],
            "rates_statuses_stats": [
                {"name": "completed", "value": 300}
            ]
        }
        """.trimIndent()

        val details = gson.fromJson(json, AnimeDetailsResponse::class.java)

        assertEquals(12345L, details.id)
        assertEquals("Steins;Gate", details.name)
        assertEquals("Штей;нс;Гейт", details.nameRu)
        assertEquals("/animes/12345", details.url)
        assertEquals(AnimeType.TV, details.type)
        assertEquals(Status.RELEASED, details.status)
        assertEquals(24, details.episodes)
        assertEquals(24, details.episodesAired)
        assertEquals(AgeRating.R, details.ageRating)
        assertEquals("9.07", details.score)
        assertEquals(24, details.duration)
        assertEquals("A thrilling sci-fi story.", details.description)
        assertEquals("<p>A thrilling sci-fi story.</p>", details.descriptionHtml)
        assertEquals("steins_gate", details.franchise)
        assertTrue(details.favoured)
        assertEquals(123L, details.topicId)

        // Names
        assertEquals(listOf("Steins;Gate"), details.namesEnglish)
        assertEquals(listOf("シュタインズ・ゲート"), details.namesJapanese)

        // Genres
        assertNotNull(details.genres)
        assertEquals(2, details.genres!!.size)
        assertEquals(1L, details.genres[0].id)
        assertEquals("Sci-Fi", details.genres[0].name)
        assertEquals("Научная фантастика", details.genres[0].nameRu)
        assertEquals("genre", details.genres[0].type)

        // User rate
        assertNotNull(details.userRate)
        assertEquals(456L, details.userRate!!.id)
        assertEquals(789L, details.userRate.userId)
        assertEquals(9.0, details.userRate.score!!, 0.001)
        assertEquals(RateStatus.COMPLETED, details.userRate.status)
        assertEquals("Great show!", details.userRate.text)

        // Videos
        assertNotNull(details.videoResponses)
        assertEquals(2, details.videoResponses!!.size)
        assertEquals(1L, details.videoResponses[0].id)
        assertEquals("Opening 1", details.videoResponses[0].name)
        assertEquals(AnimeVideoType.OPENING, details.videoResponses[0].type)
        assertEquals("youtube", details.videoResponses[0].hosting)
        assertEquals(AnimeVideoType.ENDING, details.videoResponses[1].type)

        // Studios
        assertNotNull(details.studioResponses)
        assertEquals(1, details.studioResponses!!.size)
        assertEquals(1L, details.studioResponses[0].id)
        assertEquals("White Fox", details.studioResponses[0].name)
        assertTrue(details.studioResponses[0].isReal)

        // Stats
        assertNotNull(details.rateScoresStats)
        assertEquals(2, details.rateScoresStats!!.size)
        assertEquals("9", details.rateScoresStats[0].name)
        assertEquals(100, details.rateScoresStats[0].value)
    }

    @Test
    fun `deserialize anime details response with null nested fields`() {
        val json = """
        {
            "id": 1,
            "name": null,
            "russian": null,
            "image": null,
            "url": null,
            "kind": null,
            "status": null,
            "episodes": 0,
            "episodes_aired": 0,
            "aired_on": null,
            "next_episode_at": null,
            "released_on": null,
            "english": null,
            "japanese": null,
            "rating": null,
            "score": null,
            "duration": 0,
            "description": null,
            "description_html": null,
            "franchise": null,
            "favoured": false,
            "topic_id": null,
            "genres": null,
            "user_rate": null,
            "videos": null,
            "studios": null,
            "rates_scores_stats": null,
            "rates_statuses_stats": null
        }
        """.trimIndent()

        val details = gson.fromJson(json, AnimeDetailsResponse::class.java)

        assertEquals(1L, details.id)
        assertNull(details.name)
        assertNull(details.nameRu)
        assertNull(details.image)
        assertNull(details.url)
        assertEquals(AnimeType.NONE, details.type)
        assertEquals(Status.NONE, details.status)
        assertEquals(0, details.episodes)
        assertEquals(0, details.episodesAired)
        assertNull(details.dateAired)
        assertNull(details.nextEpisodeDate)
        assertNull(details.dateReleased)
        assertNull(details.namesEnglish)
        assertNull(details.namesJapanese)
        assertNull(details.ageRating)
        assertNull(details.score)
        assertNull(details.description)
        assertNull(details.descriptionHtml)
        assertNull(details.franchise)
        assertFalse(details.favoured)
        assertNull(details.topicId)
        assertNull(details.genres)
        assertNull(details.userRate)
        assertNull(details.videoResponses)
        assertNull(details.studioResponses)
        assertNull(details.rateScoresStats)
        assertNull(details.rateStatusesStats)
    }

    @Test
    fun `deserialize anime video response`() {
        val json = """
        {
            "id": 42,
            "name": "Opening 2",
            "url": "https://example.com/video",
            "image_url": "https://example.com/thumb.jpg",
            "kind": "op",
            "hosting": "youtube"
        }
        """.trimIndent()

        val video = gson.fromJson(json, AnimeVideoResponse::class.java)

        assertEquals(42L, video.id)
        assertEquals("Opening 2", video.name)
        assertEquals("https://example.com/video", video.url)
        assertEquals("https://example.com/thumb.jpg", video.imageUrl)
        assertEquals(AnimeVideoType.OPENING, video.type)
        assertEquals("youtube", video.hosting)
    }

    @Test
    fun `deserialize anime video response with null kind`() {
        val json = """
        {
            "id": 1,
            "name": null,
            "url": "https://example.com/video",
            "image_url": null,
            "kind": null,
            "hosting": null
        }
        """.trimIndent()

        val video = gson.fromJson(json, AnimeVideoResponse::class.java)

        assertEquals(1L, video.id)
        assertNull(video.name)
        assertEquals(AnimeVideoType.OTHER, video.type)
        assertNull(video.hosting)
    }

    @Test
    fun `deserialize screenshot response`() {
        val json = """
        {
            "original": "https://orig.png",
            "preview": "https://prev.png"
        }
        """.trimIndent()

        val screenshot = gson.fromJson(json, ScreenshotResponse::class.java)

        assertEquals("https://orig.png", screenshot.original)
        assertEquals("https://prev.png", screenshot.preview)
    }

    @Test
    fun `deserialize screenshot response with null fields`() {
        val json = """{"original": null, "preview": null}""".trimIndent()

        val screenshot = gson.fromJson(json, ScreenshotResponse::class.java)

        assertNull(screenshot.original)
        assertNull(screenshot.preview)
    }

    @Test
    fun `deserialize empty list of anime responses`() {
        val json = """[]""".trimIndent()
        val list = gson.fromJson(json, Array<AnimeResponse>::class.java).toList()
        assertTrue(list.isEmpty())
    }
}
