package com.gnoemes.shikimori.utils.network

import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.common.data.LinkedContentResponse
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.gnoemes.shikimori.entity.user.data.UserHistoryResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import org.joda.time.DateTime
import java.lang.reflect.Type

class UserHistoryResponseSerializator : JsonDeserializer<UserHistoryResponse> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext): UserHistoryResponse {
        //generic method don't work for any class besides string.
        //https://stackoverflow.com/questions/40850733/json-deserialization-java-lang-double-cannot-be-cast-to-java-lang-long
        fun getValue(key: String): String? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<String>() {}.rawType)
            else null
        }

        fun getLongValue(key: String): Long? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<Long>() {}.rawType)
            else null
        }

        fun getDateTimeValue(key: String): DateTime? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<DateTime>() {}.rawType)
            else null
        }

        fun getAnimeValue(key: String): AnimeResponse? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<AnimeResponse>() {}.rawType)
            else null
        }

        fun getMangaValue(key: String): MangaResponse? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<MangaResponse>() {}.rawType)
            else null
        }

        fun getLinkedValue(anime: Boolean): LinkedContentResponse? {
            val key = "target"
            return if (anime) getAnimeValue(key)
            else getMangaValue(key)
        }

        val id = getLongValue("id")!!
        val dateCreated = getDateTimeValue("created_at")!!
        val description = getValue("description")!!
        //image path contains anime/manga
        val isAnime = json?.toString()?.contains("anime") ?: true
        val target = getLinkedValue(isAnime)

        return UserHistoryResponse(id, dateCreated, description, target)
    }
}