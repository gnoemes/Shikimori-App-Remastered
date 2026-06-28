package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.club.data.ClubResponse
import com.gnoemes.shikimori.entity.common.data.*
import com.gnoemes.shikimori.entity.common.domain.RelationType
import com.gnoemes.shikimori.entity.roles.data.CharacterResponse
import com.gnoemes.shikimori.entity.roles.data.PersonResponse
import com.gnoemes.shikimori.entity.studio.StudioResponse
import com.gnoemes.shikimori.entity.user.data.FavoriteResponse
import com.gnoemes.shikimori.entity.user.data.StatisticResponse
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

class CommonApiResponseTest {

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

    // ========================
    // ImageResponse
    // ========================

    @Test
    fun `deserialize image response`() {
        val json = """
        {
            "original": "https://orig.png",
            "preview": "https://prev.png",
            "x96": "https://x96.png",
            "x48": "https://x48.png"
        }
        """.trimIndent()

        val image = gson.fromJson(json, ImageResponse::class.java)

        assertEquals("https://orig.png", image.original)
        assertEquals("https://prev.png", image.preview)
        assertEquals("https://x96.png", image.x96)
        assertEquals("https://x48.png", image.x48)
    }

    @Test
    fun `deserialize image response with null fields`() {
        val json = """{"original": null, "preview": null, "x96": null, "x48": null}""".trimIndent()

        val image = gson.fromJson(json, ImageResponse::class.java)

        assertNull(image.original)
        assertNull(image.preview)
        assertNull(image.x96)
        assertNull(image.x48)
    }

    // ========================
    // GenreResponse
    // ========================

    @Test
    fun `deserialize genre response`() {
        val json = """
        {
            "id": 5,
            "name": "Romance",
            "russian": "Романтика",
            "kind": "genre"
        }
        """.trimIndent()

        val genre = gson.fromJson(json, GenreResponse::class.java)

        assertEquals(5L, genre.id)
        assertEquals("Romance", genre.name)
        assertEquals("Романтика", genre.nameRu)
        assertEquals("genre", genre.type)
    }

    @Test
    fun `deserialize genre response with null russian name`() {
        val json = """{"id": 1, "name": "Action", "russian": null, "kind": "genre"}""".trimIndent()

        val genre = gson.fromJson(json, GenreResponse::class.java)

        assertEquals(1L, genre.id)
        assertEquals("Action", genre.name)
        assertNull(genre.nameRu)
    }

    // ========================
    // LinkResponse
    // ========================

    @Test
    fun `deserialize link response`() {
        val json = """
        {
            "id": 100,
            "kind": "official_site",
            "url": "https://example.com"
        }
        """.trimIndent()

        val link = gson.fromJson(json, LinkResponse::class.java)

        assertEquals(100L, link.id)
        assertEquals("official_site", link.name)
        assertEquals("https://example.com", link.url)
    }

    // ========================
    // RolesResponse
    // ========================

    @Test
    fun `deserialize roles response with character`() {
        val json = """
        {
            "roles": ["Main", "Supporting"],
            "roles_russian": ["Главная", "Второстепенная"],
            "character": {
                "id": 500,
                "name": "Okabe Rintaro",
                "russian": "Окабэ Ринтаро",
                "image": {
                    "original": "https://char.png",
                    "preview": null,
                    "x96": null,
                    "x48": null
                },
                "url": "/characters/500"
            },
            "person": null
        }
        """.trimIndent()

        val roles = gson.fromJson(json, RolesResponse::class.java)

        assertEquals(listOf("Main", "Supporting"), roles.roles)
        assertEquals(listOf("Главная", "Второстепенная"), roles.rolesRu)
        assertNotNull(roles.character)
        assertEquals(500L, roles.character!!.id)
        assertEquals("Okabe Rintaro", roles.character.name)
        assertNull(roles.person)
    }

    @Test
    fun `deserialize roles response with person`() {
        val json = """
        {
            "roles": ["Director"],
            "roles_russian": ["Режиссёр"],
            "character": null,
            "person": {
                "id": 1000,
                "name": "Miyazaki Hayao",
                "russian": "Миядзаки Хаяо",
                "image": {
                    "original": "https://person.png",
                    "preview": null,
                    "x96": null,
                    "x48": null
                },
                "url": "/people/1000"
            }
        }
        """.trimIndent()

        val roles = gson.fromJson(json, RolesResponse::class.java)

        assertEquals(listOf("Director"), roles.roles)
        assertNull(roles.character)
        assertNotNull(roles.person)
        assertEquals(1000L, roles.person!!.id)
        assertEquals("Miyazaki Hayao", roles.person.name)
    }

    @Test
    fun `deserialize roles response with empty role lists`() {
        val json = """
        {
            "roles": [],
            "roles_russian": [],
            "character": null,
            "person": null
        }
        """.trimIndent()

        val roles = gson.fromJson(json, RolesResponse::class.java)

        assertTrue(roles.roles.isEmpty())
        assertTrue(roles.rolesRu.isEmpty())
        assertNull(roles.character)
        assertNull(roles.person)
    }

    // ========================
    // CharacterResponse
    // ========================

    @Test
    fun `deserialize character response`() {
        val json = """
        {
            "id": 500,
            "name": "Okabe Rintaro",
            "russian": "Окабэ Ринтаро",
            "image": {
                "original": "https://char.png",
                "preview": null,
                "x96": null,
                "x48": null
            },
            "url": "/characters/500"
        }
        """.trimIndent()

        val character = gson.fromJson(json, CharacterResponse::class.java)

        assertEquals(500L, character.id)
        assertEquals("Okabe Rintaro", character.name)
        assertEquals("Окабэ Ринтаро", character.nameRu)
        assertEquals("/characters/500", character.url)
        assertNotNull(character.image)
    }

    // ========================
    // PersonResponse
    // ========================

    @Test
    fun `deserialize person response`() {
        val json = """
        {
            "id": 1000,
            "name": "Miyazaki Hayao",
            "russian": null,
            "image": {
                "original": "https://person.png",
                "preview": null,
                "x96": null,
                "x48": null
            },
            "url": "/people/1000"
        }
        """.trimIndent()

        val person = gson.fromJson(json, PersonResponse::class.java)

        assertEquals(1000L, person.id)
        assertEquals("Miyazaki Hayao", person.name)
        assertNull(person.nameRu)
        assertEquals("/people/1000", person.url)
        assertNotNull(person.image)
    }

    // ========================
    // RelatedResponse
    // ========================

    @Test
    fun `deserialize related response with anime`() {
        val json = """
        {
            "relation": "Sequel",
            "relation_russian": "Сиквел",
            "anime": {
                "id": 1, "name": "Related Anime", "russian": null,
                "image": {"original": null, "preview": null, "x96": null, "x48": null},
                "url": "/animes/1", "kind": null, "score": null, "status": null,
                "episodes": 0, "episodes_aired": 0, "aired_on": null, "released_on": null
            },
            "manga": null
        }
        """.trimIndent()

        val related = gson.fromJson(json, RelatedResponse::class.java)

        assertEquals("Sequel", related.relation)
        assertEquals("Сиквел", related.relationRu)
        assertNotNull(related.anime)
        assertEquals(1L, related.anime!!.id)
        assertNull(related.manga)
    }

    @Test
    fun `deserialize related response with manga`() {
        val json = """
        {
            "relation": "Prequel",
            "relation_russian": null,
            "anime": null,
            "manga": {
                "id": 2, "name": "Related Manga", "russian": null,
                "image": {"original": null, "preview": null, "x96": null, "x48": null},
                "url": "/mangas/2", "kind": null, "score": null, "status": null,
                "volumes": 0, "chapters": 0, "aired_on": null, "released_on": null
            }
        }
        """.trimIndent()

        val related = gson.fromJson(json, RelatedResponse::class.java)

        assertEquals("Prequel", related.relation)
        assertNull(related.relationRu)
        assertNull(related.anime)
        assertNotNull(related.manga)
        assertEquals(2L, related.manga!!.id)
    }

    // ========================
    // FranchiseResponse
    // ========================

    @Test
    fun `deserialize franchise response`() {
        val json = """
        {
            "links": [
                {
                    "id": 1,
                    "source_id": 100,
                    "target_id": 200,
                    "source": 0,
                    "target": 1,
                    "weight": 10,
                    "relation": "sequel"
                },
                {
                    "id": 2,
                    "source_id": 200,
                    "target_id": 300,
                    "source": 1,
                    "target": 2,
                    "weight": 5,
                    "relation": "prequel"
                }
            ],
            "nodes": [
                {
                    "id": 100,
                    "date": 1262304000,
                    "name": "First Entry",
                    "image_url": "https://img.png",
                    "url": "/animes/100",
                    "year": 2010,
                    "kind": "tv",
                    "weight": 10
                },
                {
                    "id": 200,
                    "date": 1293840000,
                    "name": "Second Entry",
                    "image_url": null,
                    "url": "/animes/200",
                    "year": 2011,
                    "kind": "movie",
                    "weight": 20
                }
            ]
        }
        """.trimIndent()

        val franchise = gson.fromJson(json, FranchiseResponse::class.java)

        // Relations
        assertEquals(2, franchise.relations.size)
        assertEquals(1L, franchise.relations[0].id)
        assertEquals(100L, franchise.relations[0].sourceId)
        assertEquals(200L, franchise.relations[0].targetId)
        assertEquals(0, franchise.relations[0].sourceNodeIndex)
        assertEquals(1, franchise.relations[0].targetNodeIndex)
        assertEquals(10, franchise.relations[0].weight)
        assertEquals(RelationType.SEQUEL, franchise.relations[0].relation)
        assertEquals(RelationType.PREQUEL, franchise.relations[1].relation)

        // Nodes
        assertEquals(2, franchise.nodes.size)
        assertEquals(100L, franchise.nodes[0].id)
        assertEquals(1262304000L, franchise.nodes[0].date)
        assertEquals("First Entry", franchise.nodes[0].name)
        assertEquals("https://img.png", franchise.nodes[0].imageUrl)
        assertEquals("/animes/100", franchise.nodes[0].url)
        assertEquals(2010, franchise.nodes[0].year)
        assertEquals("tv", franchise.nodes[0].type)
        assertEquals(10, franchise.nodes[0].weight)

        assertEquals(200L, franchise.nodes[1].id)
        assertNull(franchise.nodes[1].imageUrl)
        assertEquals("movie", franchise.nodes[1].type)
    }

    @Test
    fun `deserialize franchise response with relation none`() {
        val json = """
        {
            "links": [
                {
                    "id": 1,
                    "source_id": 100,
                    "target_id": 200,
                    "source": 0,
                    "target": 1,
                    "weight": 10,
                    "relation": null
                }
            ],
            "nodes": []
        }
        """.trimIndent()

        val franchise = gson.fromJson(json, FranchiseResponse::class.java)

        assertEquals(1, franchise.relations.size)
        assertEquals(RelationType.OTHER, franchise.relations[0].relation)
        assertTrue(franchise.nodes.isEmpty())
    }

    // ========================
    // StudioResponse
    // ========================

    @Test
    fun `deserialize studio response`() {
        val json = """
        {
            "id": 1,
            "name": "White Fox",
            "filtered_name": "WhiteFox",
            "real": true,
            "image": "https://studio.png"
        }
        """.trimIndent()

        val studio = gson.fromJson(json, StudioResponse::class.java)

        assertEquals(1L, studio.id)
        assertEquals("White Fox", studio.name)
        assertEquals("WhiteFox", studio.nameFiltered)
        assertTrue(studio.isReal)
        assertEquals("https://studio.png", studio.imageUrl)
    }

    @Test
    fun `deserialize studio response with default values`() {
        val json = """
        {
            "id": 0,
            "name": "",
            "filtered_name": "",
            "real": false,
            "image": null
        }
        """.trimIndent()

        val studio = gson.fromJson(json, StudioResponse::class.java)

        assertEquals(0L, studio.id)
        assertEquals("", studio.name)
        assertFalse(studio.isReal)
        assertNull(studio.imageUrl)
    }

    // ========================
    // ClubResponse
    // ========================

    @Test
    fun `deserialize club response`() {
        val json = """
        {
            "id": 50,
            "name": "Anime Club",
            "logo": {
                "original": "https://logo.png",
                "preview": null,
                "x96": null,
                "x48": null
            },
            "is_censored": false,
            "join_policy": "free",
            "comment_policy": "free"
        }
        """.trimIndent()

        val club = gson.fromJson(json, ClubResponse::class.java)

        assertEquals(50L, club.id)
        assertEquals("Anime Club", club.name)
        assertFalse(club.isCensored)
        assertNotNull(club.image)
        assertEquals("https://logo.png", club.image.original)
        assertTrue(club is LinkedContentResponse)
    }

    @Test
    fun `deserialize club response with null policies`() {
        val json = """
        {
            "id": 0,
            "name": "",
            "logo": {
                "original": null,
                "preview": null,
                "x96": null,
                "x48": null
            },
            "is_censored": true,
            "join_policy": null,
            "comment_policy": null
        }
        """.trimIndent()

        val club = gson.fromJson(json, ClubResponse::class.java)

        assertEquals(0L, club.id)
        assertTrue(club.isCensored)
        assertNull(club.policyJoin)
        assertNull(club.policyComment)
    }

    // ========================
    // StatisticResponse
    // ========================

    @Test
    fun `deserialize statistic response`() {
        val json = """{"name": "completed", "value": 150}""".trimIndent()

        val stat = gson.fromJson(json, StatisticResponse::class.java)

        assertEquals("completed", stat.name)
        assertEquals(150, stat.value)
    }

    // ========================
    // FavoriteResponse
    // ========================

    @Test
    fun `deserialize favorite response`() {
        val json = """
        {
            "id": 1,
            "name": "Steins;Gate",
            "russian": null,
            "image": "https://img.png",
            "url": "/animes/1"
        }
        """.trimIndent()

        val favorite = gson.fromJson(json, FavoriteResponse::class.java)

        assertEquals(1L, favorite.id)
        assertEquals("Steins;Gate", favorite.name)
        assertNull(favorite.nameRu)
        assertEquals("https://img.png", favorite.image)
        assertEquals("/animes/1", favorite.url)
    }
}
