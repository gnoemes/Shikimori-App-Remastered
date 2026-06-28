package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.common.domain.AgeRating
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.manga.data.MangaDetailsResponse
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.gnoemes.shikimori.entity.manga.domain.MangaType
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

class MangaApiResponseTest {

    private lateinit var gson: com.google.gson.Gson

    @BeforeEach
    fun setUp() {
        gson = GsonBuilder()
            .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer())
            .create()
    }

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
    fun `deserialize manga response`() {
        val json = """
        {
            "id": 67890,
            "name": "Berserk",
            "russian": "Берсерк",
            "image": {
                "original": "https://orig.png",
                "preview": "https://prev.png",
                "x96": null,
                "x48": null
            },
            "url": "/mangas/67890-berserk",
            "kind": "manga",
            "score": 9.0,
            "status": "ongoing",
            "volumes": 40,
            "chapters": 370,
            "aired_on": "1990-01-01",
            "released_on": null
        }
        """.trimIndent()

        val manga = gson.fromJson(json, MangaResponse::class.java)

        assertEquals(67890L, manga.id)
        assertEquals("Berserk", manga.name)
        assertEquals("Берсерк", manga.nameRu)
        assertEquals("/mangas/67890-berserk", manga.url)
        assertEquals(MangaType.MANGA, manga.type)
        assertEquals(9.0, manga.score!!, 0.001)
        assertEquals(Status.ONGOING, manga.status)
        assertEquals(40, manga.volumes)
        assertEquals(370, manga.chapters)
        assertNotNull(manga.dateAired)
        assertNull(manga.dateReleased)
    }

    @Test
    fun `deserialize manga response with minimal fields`() {
        val json = """
        {
            "id": 1,
            "name": "Minimal Manga",
            "russian": null,
            "image": {
                "original": null,
                "preview": null,
                "x96": null,
                "x48": null
            },
            "url": "/mangas/1",
            "kind": null,
            "score": null,
            "status": null,
            "volumes": 0,
            "chapters": 0,
            "aired_on": null,
            "released_on": null
        }
        """.trimIndent()

        val manga = gson.fromJson(json, MangaResponse::class.java)

        assertEquals(1L, manga.id)
        assertEquals("Minimal Manga", manga.name)
        assertNull(manga.nameRu)
        assertEquals(MangaType.UNKNOWN, manga.type)
        assertNull(manga.score)
        assertEquals(Status.NONE, manga.status)
        assertEquals(0, manga.volumes)
        assertEquals(0, manga.chapters)
        assertNull(manga.dateAired)
        assertNull(manga.dateReleased)
    }

    @Test
    fun `deserialize manga details response`() {
        val json = """
        {
            "id": 67890,
            "name": "Berserk",
            "russian": "Берсерк",
            "image": {
                "original": "https://orig.png",
                "preview": "https://prev.png",
                "x96": null,
                "x48": null
            },
            "url": "/mangas/67890",
            "kind": "manga",
            "status": "ongoing",
            "volumes": 40,
            "chapters": 370,
            "aired_on": "1990-01-01",
            "released_on": null,
            "english": ["Berserk"],
            "japanese": ["ベルセルク"],
            "rating": "r",
            "score": "9.0",
            "description": "A dark fantasy manga.",
            "description_html": "<p>A dark fantasy manga.</p>",
            "franchise": "berserk",
            "favoured": true,
            "topic_id": 456,
            "genres": [
                {"id": 1, "name": "Action", "russian": "Экшен", "kind": "genre"},
                {"id": 2, "name": "Fantasy", "russian": "Фэнтези", "kind": "genre"}
            ],
            "user_rate": null,
            "rates_scores_stats": [
                {"name": "10", "value": 500},
                {"name": "9", "value": 300}
            ],
            "rates_statuses_stats": [
                {"name": "completed", "value": 2000}
            ]
        }
        """.trimIndent()

        val details = gson.fromJson(json, MangaDetailsResponse::class.java)

        assertEquals(67890L, details.id)
        assertEquals("Berserk", details.name)
        assertEquals("Берсерк", details.nameRu)
        assertEquals(MangaType.MANGA, details.type)
        assertEquals(Status.ONGOING, details.status)
        assertEquals(40, details.volumes)
        assertEquals(370, details.chapters)
        assertEquals(AgeRating.R, details.ageRating)
        assertEquals("9.0", details.score)
        assertEquals("A dark fantasy manga.", details.description)
        assertEquals("berserk", details.franchise)
        assertTrue(details.favoured)

        // Genres
        assertNotNull(details.genres)
        assertEquals(2, details.genres!!.size)
        assertEquals("Action", details.genres[0].name)

        // User rate is null
        assertNull(details.userRate)

        // Stats
        assertNotNull(details.rateScoresStats)
        assertEquals(2, details.rateScoresStats!!.size)
        assertEquals(500, details.rateScoresStats[0].value)
    }

    @Test
    fun `deserialize manga details response with null fields`() {
        val json = """
        {
            "id": 1,
            "name": null,
            "russian": null,
            "image": null,
            "url": null,
            "kind": null,
            "status": null,
            "volumes": 0,
            "chapters": 0,
            "aired_on": null,
            "released_on": null,
            "english": null,
            "japanese": null,
            "rating": null,
            "score": null,
            "description": null,
            "description_html": null,
            "franchise": null,
            "favoured": false,
            "topic_id": null,
            "genres": null,
            "user_rate": null,
            "rates_scores_stats": null,
            "rates_statuses_stats": null
        }
        """.trimIndent()

        val details = gson.fromJson(json, MangaDetailsResponse::class.java)

        assertEquals(1L, details.id)
        assertNull(details.name)
        assertNull(details.nameRu)
        assertNull(details.image)
        assertNull(details.url)
        assertEquals(MangaType.UNKNOWN, details.type)
        assertEquals(Status.NONE, details.status)
        assertNull(details.ageRating)
        assertNull(details.score)
        assertNull(details.genres)
        assertNull(details.userRate)
        assertFalse(details.favoured)
    }
}
