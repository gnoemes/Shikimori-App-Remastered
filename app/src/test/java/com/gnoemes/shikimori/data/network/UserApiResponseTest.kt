package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.common.domain.Type as ShikimoriType
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.user.data.*
import com.gnoemes.shikimori.entity.user.domain.MessageType
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

class UserApiResponseTest {

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
    fun `deserialize user brief response`() {
        val json = """
        {
            "id": 1,
            "nickname": "TestUser",
            "avatar": "https://avatars.com/test.jpg",
            "image": {
                "x160": "https://x160.jpg",
                "x148": "https://x148.jpg",
                "x80": "https://x80.jpg",
                "x64": "https://x64.jpg",
                "x48": "https://x48.jpg",
                "x32": "https://x32.jpg",
                "x16": "https://x16.jpg"
            },
            "last_online_at": "2024-01-15T10:30:00.000+03:00",
            "name": "Test User",
            "sex": "male",
            "website": "https://example.com",
            "birth_on": "1990-01-01",
            "locale": "ru"
        }
        """.trimIndent()

        val user = gson.fromJson(json, UserBriefResponse::class.java)

        assertEquals(1L, user.id)
        assertEquals("TestUser", user.nickname)
        assertEquals("https://avatars.com/test.jpg", user.avatar)
        assertEquals("Test User", user.name)
        assertEquals("male", user.sex)
        assertEquals("https://example.com", user.website)
        assertEquals("ru", user.locale)
        assertNotNull(user.dateLastOnline)
        assertNotNull(user.dateBirth)

        // Image assertions
        assertNotNull(user.image)
        assertEquals("https://x160.jpg", user.image.x160)
        assertEquals("https://x148.jpg", user.image.x148)
        assertEquals("https://x80.jpg", user.image.x80)
        assertEquals("https://x64.jpg", user.image.x64)
        assertEquals("https://x48.jpg", user.image.x48)
        assertEquals("https://x32.jpg", user.image.x32)
        assertEquals("https://x16.jpg", user.image.x16)
    }

    @Test
    fun `deserialize user brief response with minimal fields`() {
        val json = """
        {
            "id": 2,
            "nickname": "MinimalUser",
            "avatar": null,
            "image": {
                "x160": null,
                "x148": null,
                "x80": null,
                "x64": null,
                "x48": null,
                "x32": null,
                "x16": null
            },
            "last_online_at": "2024-01-15T10:30:00.000+03:00",
            "name": null,
            "sex": null,
            "website": null,
            "birth_on": null,
            "locale": null
        }
        """.trimIndent()

        val user = gson.fromJson(json, UserBriefResponse::class.java)

        assertEquals(2L, user.id)
        assertEquals("MinimalUser", user.nickname)
        assertNull(user.avatar)
        assertNull(user.name)
        assertNull(user.sex)
        assertNull(user.website)
        assertNull(user.dateBirth)
        assertNull(user.locale)
        assertNotNull(user.dateLastOnline)
    }

    @Test
    fun `deserialize user details response`() {
        val json = """
        {
            "id": 1,
            "nickname": "DetailedUser",
            "image": {
                "x160": "https://x160.jpg",
                "x148": null,
                "x80": null,
                "x64": null,
                "x48": null,
                "x32": null,
                "x16": null
            },
            "last_online_at": "2024-01-15T10:30:00.000+03:00",
            "name": "Detailed Name",
            "sex": "female",
            "website": "https://blog.example.com",
            "birth_on": "1995-06-15",
            "locale": "en",
            "full_years": 29,
            "last_online": "2024-01-15 10:30",
            "location": "Moscow",
            "banned": false,
            "about": "Anime fan",
            "common_info": ["Male", "24 years", "Moscow"],
            "show_comments": true,
            "in_friends": true,
            "is_ignored": false,
            "stats": {
                "full_statuses": {
                    "anime": [
                        {"id": 1, "name": "completed", "size": 100, "type": "Anime"},
                        {"id": 2, "name": "watching", "size": 20, "type": "Anime"}
                    ],
                    "manga": [
                        {"id": 3, "name": "completed", "size": 50, "type": "Manga"}
                    ]
                },
                "scores": {
                    "anime": [
                        {"name": "10", "value": 5},
                        {"name": "9", "value": 10}
                    ],
                    "manga": [
                        {"name": "10", "value": 3}
                    ]
                },
                "types": {
                    "anime": [
                        {"name": "tv", "value": 80}
                    ],
                    "manga": [
                        {"name": "manga", "value": 30}
                    ]
                },
                "ratings": {
                    "anime": [
                        {"name": "none", "value": 50}
                    ],
                    "manga": null
                },
                "has_anime?": true,
                "has_manga?": true
            }
        }
        """.trimIndent()

        val details = gson.fromJson(json, UserDetailsResponse::class.java)

        assertEquals(1L, details.id)
        assertEquals("DetailedUser", details.nickname)
        assertEquals("Detailed Name", details.name)
        assertEquals("female", details.sex)
        assertEquals(29, details.fullYears)
        assertEquals("2024-01-15 10:30", details.lastOnline)
        assertEquals("Moscow", details.location)
        assertFalse(details.isBanned)
        assertEquals("Anime fan", details.about)
        assertEquals(listOf("Male", "24 years", "Moscow"), details.commonInfo)
        assertTrue(details.isShowComments)
        assertTrue(details.isFriend)
        assertFalse(details.isIgnored)

        // Stats
        assertNotNull(details.stats)
        assertTrue(details.stats.hasAnime)
        assertTrue(details.stats.hasManga)

        // Full statuses
        assertEquals(2, details.stats.status.anime.size)
        assertEquals(RateStatus.COMPLETED, details.stats.status.anime[0].name)
        assertEquals(100, details.stats.status.anime[0].size)
        assertEquals(ShikimoriType.ANIME, details.stats.status.anime[0].type)

        // Scores
        assertEquals(2, details.stats.scores.anime.size)
        assertEquals("10", details.stats.scores.anime[0].name)
        assertEquals(5, details.stats.scores.anime[0].value)

        // Ratings manga is null
        assertNull(details.stats.ratings.manga)
    }

    @Test
    fun `deserialize user history response`() {
        val json = """
        {
            "id": 100,
            "created_at": "2024-01-15T10:30:00.000+03:00",
            "description": "Watched episode 1",
            "target": null
        }
        """.trimIndent()

        val history = gson.fromJson(json, UserHistoryResponse::class.java)

        assertEquals(100L, history.id)
        assertEquals("Watched episode 1", history.description)
        assertNotNull(history.dateCreated)
        assertNull(history.target)
    }

    @Test
    fun `deserialize message response`() {
        val json = """
        {
            "id": 500,
            "kind": "inbox",
            "read": false,
            "body": "Hello!",
            "html_body": "<p>Hello!</p>",
            "created_at": "2024-01-15T10:30:00.000+03:00",
            "linked": null,
            "from": {
                "id": 1,
                "nickname": "Sender",
                "avatar": null,
                "image": {
                    "x160": null,
                    "x148": null,
                    "x80": null,
                    "x64": null,
                    "x48": null,
                    "x32": null,
                    "x16": null
                },
                "last_online_at": "2024-01-15T10:30:00.000+03:00",
                "name": null,
                "sex": null,
                "website": null,
                "birth_on": null,
                "locale": null
            },
            "to": {
                "id": 2,
                "nickname": "Receiver",
                "avatar": null,
                "image": {
                    "x160": null,
                    "x148": null,
                    "x80": null,
                    "x64": null,
                    "x48": null,
                    "x32": null,
                    "x16": null
                },
                "last_online_at": "2024-01-15T12:00:00.000+03:00",
                "name": null,
                "sex": null,
                "website": null,
                "birth_on": null,
                "locale": null
            }
        }
        """.trimIndent()

        val message = gson.fromJson(json, MessageResponse::class.java)

        assertEquals(500L, message.id)
        assertEquals(MessageType.INBOX, message.type)
        assertFalse(message.read)
        assertEquals("Hello!", message.body)
        assertEquals("<p>Hello!</p>", message.htmlBody)
        assertNotNull(message.dateCreated)
        assertNull(message.linked)
        assertEquals(1L, message.userFrom.id)
        assertEquals("Sender", message.userFrom.nickname)
        assertEquals(2L, message.userTo.id)
        assertEquals("Receiver", message.userTo.nickname)
    }

    @Test
    fun `deserialize favorite list response`() {
        val json = """
        {
            "animes": [
                {"id": 1, "name": "Anime 1", "russian": null, "image": "img1.jpg", "url": null},
                {"id": 2, "name": "Anime 2", "russian": "Аниме 2", "image": "img2.jpg", "url": "/animes/2"}
            ],
            "mangas": [],
            "characters": [
                {"id": 10, "name": "Char 1", "russian": null, "image": "char1.jpg", "url": null}
            ],
            "people": [],
            "mangakas": [],
            "seyu": [],
            "producers": []
        }
        """.trimIndent()

        val favorites = gson.fromJson(json, FavoriteListResponse::class.java)

        assertEquals(2, favorites.animes.size)
        assertEquals(1L, favorites.animes[0].id)
        assertEquals("Anime 1", favorites.animes[0].name)
        assertEquals("img1.jpg", favorites.animes[0].image)

        assertEquals(0, favorites.mangas.size)
        assertEquals(1, favorites.characters.size)
        assertEquals("Char 1", favorites.characters[0].name)
    }

    @Test
    fun `deserialize unread messages count`() {
        val json = """{"messages": 5, "news": 2, "notifications": 0}""".trimIndent()

        val count = gson.fromJson(json, UserUnreadMessagesCount::class.java)

        assertEquals(5, count.messages)
        assertEquals(2, count.news)
        assertEquals(0, count.notifications)
    }

    @Test
    fun `deserialize user image response`() {
        val json = """
        {
            "x160": "https://img.jpg",
            "x148": null,
            "x80": null,
            "x64": null,
            "x48": null,
            "x32": null,
            "x16": null
        }
        """.trimIndent()

        val image = gson.fromJson(json, UserImageResponse::class.java)

        assertEquals("https://img.jpg", image.x160)
        assertNull(image.x148)
        assertNull(image.x80)
        assertNull(image.x64)
        assertNull(image.x48)
        assertNull(image.x32)
        assertNull(image.x16)
    }

    @Test
    fun `deserialize list of user brief responses`() {
        val json = """
        [
            {
                "id": 1,
                "nickname": "User1",
                "avatar": null,
                "image": {"x160": null, "x148": null, "x80": null, "x64": null, "x48": null, "x32": null, "x16": null},
                "last_online_at": "2024-01-15T10:30:00.000+03:00",
                "name": null,
                "sex": null,
                "website": null,
                "birth_on": null,
                "locale": null
            },
            {
                "id": 2,
                "nickname": "User2",
                "avatar": null,
                "image": {"x160": null, "x148": null, "x80": null, "x64": null, "x48": null, "x32": null, "x16": null},
                "last_online_at": "2024-01-16T10:30:00.000+03:00",
                "name": null,
                "sex": null,
                "website": null,
                "birth_on": null,
                "locale": null
            }
        ]
        """.trimIndent()

        val users = gson.fromJson(json, Array<UserBriefResponse>::class.java).toList()

        assertEquals(2, users.size)
        assertEquals("User1", users[0].nickname)
        assertEquals("User2", users[1].nickname)
    }
}
