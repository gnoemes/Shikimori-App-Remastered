package com.gnoemes.shikimori.data.repository.app.impl

import android.content.SharedPreferences
import com.gnoemes.shikimori.data.repository.app.TokenSource
import com.gnoemes.shikimori.di.app.annotations.UserQualifier
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Token
import com.gnoemes.shikimori.utils.putString
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Completable
import javax.inject.Inject

class TokenSourceImpl @Inject constructor(
        @UserQualifier private val preferences: SharedPreferences,
        private val gson: Gson
) : TokenSource {

    override fun saveToken(token: Token?): Completable = Completable.fromAction {
        val json = gson.toJson(token)
        val value = if (json == "null") null else json
        preferences.putString(AppExtras.ARGUMENT_TOKEN, value)
    }

    override fun getToken(): Token? {
        return try {
            val json = preferences.getString(AppExtras.ARGUMENT_TOKEN, "")
            when (!json.isNullOrEmpty()) {
                true -> gson.fromJson<Token>(json, Token::class.java)
                else -> null
            }
        } catch (e: JsonSyntaxException) {
            return null
        }
    }

    override fun isTokenExists(): Boolean = preferences.contains(AppExtras.ARGUMENT_TOKEN)
}