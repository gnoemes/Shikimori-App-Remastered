package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.common.domain.Type as ShikimoriType
import com.gnoemes.shikimori.entity.rates.data.RateResponse
import com.gnoemes.shikimori.entity.rates.data.UserRateResponse
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
import java.util.Date

class RateApiResponseTest {

    private lateinit var gson: com.google.gson.Gson

    @BeforeEach
    fun setUp() {
        gson = GsonBuilder()
            .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer())
            .create()
    }

    private class DateTimeDeserializer : JsonDeserializer<DateTime> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: java.lang.reflect.Type, context: JsonDeserializationContext): DateTime {
            return try {
                DateTime(json.asString, DateTimeZone.UTC).withZone(DateTimeZone.getDefault())
            } catch (e: IllegalArgumentException) {
                val date: Date = context.deserialize(json, Date::class.java)
                DateTime(date)
            }
        }
    }

    @Test
    fun `deserialize rate response with anime`() {
        val json = """
        {
            "id": 1000,
            "score": 9,
            "status": "completed",
            "text": "Amazing anime!",
            "text_html": "<p>Amazing anime!</p>",
            "episodes": 24,
            "chapters": null,
            "volumes": null,
            "rewatches": 2,
            "created_at": "2024-01-15T10:30:00.000+03:00",
            "updated_at": "2024-01-20T12:00:00.000+03:00",
            "anime": {
                "id": 12345,
                "name": "Steins;Gate",
                "russian": null,
                "image": {
                    "original": null,
                    "preview": null,
                    "x96": null,
                    "x48": null
                },
                "url": "/animes/12345",
                "kind": "tv",
                "score": 9.07,
                "status": "released",
                "episodes": 24,
                "episodes_aired": 24,
                "aired_on": null,
                "released_on": null
            },
            "manga": null
        }
        """.trimIndent()

        val rate = gson.fromJson(json, RateResponse::class.java)

        assertEquals(1000L, rate.id)
        assertEquals(9, rate.score)
        assertEquals(RateStatus.COMPLETED, rate.status)
        assertEquals("Amazing anime!", rate.text)
        assertEquals("<p>Amazing anime!</p>", rate.textHtml)
        assertEquals(24, rate.episodes)
        assertNull(rate.chapters)
        assertNull(rate.volumes)
        assertEquals(2, rate.rewatches)
        assertNotNull(rate.createdDateTime)
        assertNotNull(rate.updatedDateTime)

        // Anime
        assertNotNull(rate.anime)
        assertEquals(12345L, rate.anime!!.id)
        assertEquals("Steins;Gate", rate.anime.name)

        // Manga should be null
        assertNull(rate.manga)
    }

    @Test
    fun `deserialize rate response with manga`() {
        val json = """
        {
            "id": 2000,
            "score": 8,
            "status": "watching",
            "text": null,
            "text_html": null,
            "episodes": null,
            "chapters": 15,
            "volumes": 2,
            "rewatches": null,
            "created_at": null,
            "updated_at": null,
            "anime": null,
            "manga": {
                "id": 67890,
                "name": "Berserk",
                "russian": "Берсерк",
                "image": {
                    "original": null,
                    "preview": null,
                    "x96": null,
                    "x48": null
                },
                "url": "/mangas/67890",
                "kind": "manga",
                "score": 9.0,
                "status": "ongoing",
                "volumes": 40,
                "chapters": 370,
                "aired_on": null,
                "released_on": null
            }
        }
        """.trimIndent()

        val rate = gson.fromJson(json, RateResponse::class.java)

        assertEquals(2000L, rate.id)
        assertEquals(8, rate.score)
        assertEquals(RateStatus.WATCHING, rate.status)
        assertNull(rate.text)
        assertNull(rate.episodes)
        assertEquals(15, rate.chapters)
        assertEquals(2, rate.volumes)
        assertNull(rate.rewatches)
        assertNull(rate.createdDateTime)
        assertNull(rate.updatedDateTime)

        // Anime should be null
        assertNull(rate.anime)

        // Manga
        assertNotNull(rate.manga)
        assertEquals(67890L, rate.manga!!.id)
        assertEquals("Berserk", rate.manga.name)
    }

    @Test
    fun `deserialize user rate response`() {
        val json = """
        {
            "id": 5000,
            "user_id": 100,
            "target_id": 12345,
            "target_type": "Anime",
            "score": 9.0,
            "status": "completed",
            "rewatches": 1,
            "episodes": 24,
            "volumes": null,
            "chapters": null,
            "text": "Great!",
            "text_html": "<p>Great!</p>",
            "created_at": "2024-01-15T10:30:00.000+03:00",
            "updated_at": "2024-01-20T12:00:00.000+03:00"
        }
        """.trimIndent()

        val rate = gson.fromJson(json, UserRateResponse::class.java)

        assertEquals(5000L, rate.id)
        assertEquals(100L, rate.userId)
        assertEquals(12345L, rate.targetId)
        assertEquals(ShikimoriType.ANIME, rate.targetType)
        assertEquals(9.0, rate.score!!, 0.001)
        assertEquals(RateStatus.COMPLETED, rate.status)
        assertEquals(1, rate.rewatches)
        assertEquals(24, rate.episodes)
        assertNull(rate.volumes)
        assertNull(rate.chapters)
        assertEquals("Great!", rate.text)
        assertEquals("<p>Great!</p>", rate.textHtml)
        assertNotNull(rate.dateCreated)
        assertNotNull(rate.dateUpdated)
    }

    @Test
    fun `deserialize user rate response with minimal fields`() {
        val json = """
        {
            "id": null,
            "user_id": null,
            "target_id": null,
            "target_type": null,
            "score": null,
            "status": null,
            "rewatches": null,
            "episodes": null,
            "volumes": null,
            "chapters": null,
            "text": null,
            "text_html": null,
            "created_at": null,
            "updated_at": null
        }
        """.trimIndent()

        val rate = gson.fromJson(json, UserRateResponse::class.java)

        assertNull(rate.id)
        assertNull(rate.userId)
        assertNull(rate.targetId)
        assertNull(rate.targetType)
        assertNull(rate.score)
        assertNull(rate.status)
        assertNull(rate.rewatches)
        assertNull(rate.episodes)
        assertNull(rate.volumes)
        assertNull(rate.chapters)
        assertNull(rate.text)
        assertNull(rate.textHtml)
        assertNull(rate.dateCreated)
        assertNull(rate.dateUpdated)
    }
}
