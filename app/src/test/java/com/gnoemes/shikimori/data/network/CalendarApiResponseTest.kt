package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.calendar.data.CalendarResponse
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

class CalendarApiResponseTest {

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
    fun `deserialize calendar response`() {
        val json = """
        {
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
            "next_episode": 13,
            "next_episode_at": "2024-01-20T15:00:00.000+03:00",
            "duration": "24"
        }
        """.trimIndent()

        val calendar = gson.fromJson(json, CalendarResponse::class.java)

        assertEquals(12345L, calendar.anime.id)
        assertEquals("Steins;Gate", calendar.anime.name)
        assertEquals(13, calendar.nextEpisode)
        assertNotNull(calendar.nextEpisodeDate)
        assertEquals("24", calendar.duration)
    }

    @Test
    fun `deserialize calendar response with null next episode date`() {
        val json = """
        {
            "anime": {
                "id": 1,
                "name": "Test",
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
                "episodes": 0,
                "episodes_aired": 0,
                "aired_on": null,
                "released_on": null
            },
            "next_episode": 1,
            "next_episode_at": null,
            "duration": null
        }
        """.trimIndent()

        val calendar = gson.fromJson(json, CalendarResponse::class.java)

        assertEquals(1L, calendar.anime.id)
        assertEquals(1, calendar.nextEpisode)
        assertNull(calendar.nextEpisodeDate)
        assertNull(calendar.duration)
    }

    @Test
    fun `deserialize list of calendar responses`() {
        val json = """
        [
            {
                "anime": {
                    "id": 1, "name": "Anime A", "russian": null,
                    "image": {"original": null, "preview": null, "x96": null, "x48": null},
                    "url": "/animes/1", "kind": null, "score": null, "status": null,
                    "episodes": 0, "episodes_aired": 0, "aired_on": null, "released_on": null
                },
                "next_episode": 5,
                "next_episode_at": null,
                "duration": null
            },
            {
                "anime": {
                    "id": 2, "name": "Anime B", "russian": null,
                    "image": {"original": null, "preview": null, "x96": null, "x48": null},
                    "url": "/animes/2", "kind": null, "score": null, "status": null,
                    "episodes": 0, "episodes_aired": 0, "aired_on": null, "released_on": null
                },
                "next_episode": 10,
                "next_episode_at": null,
                "duration": null
            }
        ]
        """.trimIndent()

        val calendars = gson.fromJson(json, Array<CalendarResponse>::class.java).toList()

        assertEquals(2, calendars.size)
        assertEquals("Anime A", calendars[0].anime.name)
        assertEquals(5, calendars[0].nextEpisode)
        assertEquals("Anime B", calendars[1].anime.name)
        assertEquals(10, calendars[1].nextEpisode)
    }
}
