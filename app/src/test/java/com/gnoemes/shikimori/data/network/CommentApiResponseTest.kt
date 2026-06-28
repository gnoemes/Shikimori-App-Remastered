package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.comment.data.CommentResponse
import com.gnoemes.shikimori.entity.comment.domain.CommentableType
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

class CommentApiResponseTest {

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
    fun `deserialize comment response`() {
        val json = """
        {
            "id": 999,
            "user_id": 42,
            "commentable_id": 123,
            "commentable_type": "Topic",
            "body": "Great episode!",
            "html_body": "<p>Great episode!</p>",
            "created_at": "2024-01-15T10:30:00.000+03:00",
            "updated_at": "2024-01-15T11:00:00.000+03:00",
            "is_offtopic": false,
            "is_summary": false,
            "can_be_edited": true,
            "user": {
                "id": 42,
                "nickname": "Commentator",
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
        }
        """.trimIndent()

        val comment = gson.fromJson(json, CommentResponse::class.java)

        assertEquals(999L, comment.id)
        assertEquals(42L, comment.userId)
        assertEquals(123L, comment.commentableId)
        assertEquals(CommentableType.TOPIC, comment.commentableType)
        assertEquals("Great episode!", comment.body)
        assertEquals("<p>Great episode!</p>", comment.bodyHtml)
        assertNotNull(comment.dateCreated)
        assertNotNull(comment.dateUpdated)
        assertFalse(comment.isOfftopic)
        assertFalse(comment.isSummary)
        assertTrue(comment.isEditable)

        // User
        assertEquals(42L, comment.user.id)
        assertEquals("Commentator", comment.user.nickname)
    }

    @Test
    fun `deserialize comment response with null body`() {
        val json = """
        {
            "id": 1,
            "user_id": 1,
            "commentable_id": 1,
            "commentable_type": "User",
            "body": null,
            "html_body": null,
            "created_at": "2024-01-15T10:30:00.000+03:00",
            "updated_at": "2024-01-15T10:30:00.000+03:00",
            "is_offtopic": true,
            "is_summary": true,
            "can_be_edited": false,
            "user": {
                "id": 1,
                "nickname": "User",
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
        }
        """.trimIndent()

        val comment = gson.fromJson(json, CommentResponse::class.java)

        assertEquals(1L, comment.id)
        assertEquals(CommentableType.USER, comment.commentableType)
        assertNull(comment.body)
        assertNull(comment.bodyHtml)
        assertTrue(comment.isOfftopic)
        assertTrue(comment.isSummary)
        assertFalse(comment.isEditable)
    }

    @Test
    fun `deserialize list of comments`() {
        val json = """
        [
            {
                "id": 1,
                "user_id": 1,
                "commentable_id": 10,
                "commentable_type": "Topic",
                "body": "First!",
                "html_body": "<p>First!</p>",
                "created_at": "2024-01-15T10:30:00.000+03:00",
                "updated_at": "2024-01-15T10:30:00.000+03:00",
                "is_offtopic": false,
                "is_summary": false,
                "can_be_edited": true,
                "user": {
                    "id": 1, "nickname": "User1", "avatar": null,
                    "image": {"x160": null, "x148": null, "x80": null, "x64": null, "x48": null, "x32": null, "x16": null},
                    "last_online_at": "2024-01-15T10:30:00.000+03:00",
                    "name": null, "sex": null, "website": null, "birth_on": null, "locale": null
                }
            },
            {
                "id": 2,
                "user_id": 2,
                "commentable_id": 10,
                "commentable_type": "Topic",
                "body": "Second!",
                "html_body": "<p>Second!</p>",
                "created_at": "2024-01-15T11:00:00.000+03:00",
                "updated_at": "2024-01-15T11:00:00.000+03:00",
                "is_offtopic": false,
                "is_summary": true,
                "can_be_edited": false,
                "user": {
                    "id": 2, "nickname": "User2", "avatar": null,
                    "image": {"x160": null, "x148": null, "x80": null, "x64": null, "x48": null, "x32": null, "x16": null},
                    "last_online_at": "2024-01-15T11:00:00.000+03:00",
                    "name": null, "sex": null, "website": null, "birth_on": null, "locale": null
                }
            }
        ]
        """.trimIndent()

        val comments = gson.fromJson(json, Array<CommentResponse>::class.java).toList()

        assertEquals(2, comments.size)
        assertEquals("First!", comments[0].body)
        assertTrue(comments[0].isEditable)
        assertEquals("Second!", comments[1].body)
        assertTrue(comments[1].isSummary)
        assertFalse(comments[1].isEditable)
    }
}
