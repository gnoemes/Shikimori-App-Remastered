package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.app.data.TokenResponse
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Tests for Auth API response models.
 * TokenResponse does not use DateTime, so no custom Gson adapter needed.
 */
class AuthApiResponseTest {

    private val gson = Gson()

    @Test
    fun `deserialize token response`() {
        val json = """
        {
            "access_token": "abc123def456",
            "refresh_token": "refresh789xyz"
        }
        """.trimIndent()

        val token = gson.fromJson(json, TokenResponse::class.java)

        assertEquals("abc123def456", token.accessToken)
        assertEquals("refresh789xyz", token.refreshToken)
    }

    @Test
    fun `deserialize token response with empty strings`() {
        val json = """{"access_token": "", "refresh_token": ""}""".trimIndent()

        val token = gson.fromJson(json, TokenResponse::class.java)

        assertEquals("", token.accessToken)
        assertEquals("", token.refreshToken)
    }

    @Test
    fun `deserialize token response with special characters`() {
        val json = """{"access_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0", "refresh_token": "def"}""".trimIndent()

        val token = gson.fromJson(json, TokenResponse::class.java)

        assertTrue(token.accessToken.startsWith("eyJ"))
        assertTrue(token.accessToken.contains("."))
        assertEquals("def", token.refreshToken)
    }
}
