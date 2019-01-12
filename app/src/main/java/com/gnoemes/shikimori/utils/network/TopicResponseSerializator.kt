package com.gnoemes.shikimori.utils.network

import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.club.data.ClubResponse
import com.gnoemes.shikimori.entity.common.data.LinkedContentResponse
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.forum.data.ForumResponse
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.gnoemes.shikimori.entity.roles.data.CharacterResponse
import com.gnoemes.shikimori.entity.roles.data.PersonResponse
import com.gnoemes.shikimori.entity.topic.data.TopicResponse
import com.gnoemes.shikimori.entity.user.data.UserBriefResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import org.joda.time.DateTime

class TopicResponseSerializator : JsonDeserializer<TopicResponse> {

    override fun deserialize(json: JsonElement?, typeOfT: java.lang.reflect.Type?, context: JsonDeserializationContext): TopicResponse? {

        //generic method don't work for any class besides string.
        //https://stackoverflow.com/questions/40850733/json-deserialization-java-lang-double-cannot-be-cast-to-java-lang-long
        fun getValue(key : String) : String? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<String>(){}.rawType)
            else null
        }

        fun getLongValue(key: String): Long? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<Long>() {}.rawType)
            else null
        }

        fun getBooleanValue(key: String): Boolean? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<Boolean>() {}.rawType)
            else null
        }

        fun getDateTimeValue(key : String) : DateTime? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<DateTime>() {}.rawType)
            else null
        }

        fun getForumValue(key : String) : ForumResponse? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<ForumResponse>() {}.rawType)
            else null
        }

        fun getUserValue(key : String) : UserBriefResponse? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<UserBriefResponse>() {}.rawType)
            else null
        }

        fun getTypeValue(key : String) : Type? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<Type>() {}.rawType)
            else null
        }

        fun getAnimeValue(key : String) : AnimeResponse? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<AnimeResponse>() {}.rawType)
            else null
        }

        fun getMangaValue(key : String) : MangaResponse? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<MangaResponse>() {}.rawType)
            else null
        }

        fun getPersonValue(key : String) : PersonResponse? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<PersonResponse>() {}.rawType)
            else null
        }

        fun getCharacterValue(key : String) : CharacterResponse? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<CharacterResponse>() {}.rawType)
            else null
        }

        fun getClubValue(key : String) : ClubResponse? {
            val jsonObj = json?.asJsonObject?.get(key) ?: return null
            return if (!jsonObj.isJsonNull) context.deserialize(jsonObj, object : TypeToken<ClubResponse>() {}.rawType)
            else null
        }


        fun  getLinkedValue(type : Type) : LinkedContentResponse? {
            val key= "linked"
            return when (type) {
                Type.ANIME -> getAnimeValue(key)
                Type.MANGA, Type.RANOBE -> getMangaValue(key)
                Type.PERSON -> getPersonValue(key)
                Type.CHARACTER -> getCharacterValue(key)
                Type.CLUB -> getClubValue(key)
                else -> null
            }
        }

        val id = getLongValue("id")
        val title = getValue("topic_title")
        val body = getValue("body")
        val htmlBody = getValue("html_body")
        val htmlFooter = getValue("html_footer")
        val dateCreated = getDateTimeValue("created_at")
        val commentCount = getLongValue("comments_count")
        val forum = getForumValue("forum")
        val user = getUserValue("user")
        val type = getValue("type")
        val linkedType = getTypeValue("linked_type") ?: Type.UNKNOWN
        val linked = getLinkedValue(linkedType)
        val viewed = getBooleanValue("viewed") ?: false
        val episode = getValue("episode")

        return TopicResponse(
                id!!,
                title!!,
                body,
                htmlBody,
                htmlFooter,
                dateCreated!!,
                commentCount!!,
                forum!!,
                user!!,
                type,
                linkedType,
                linked,
                viewed,
                episode

        )
    }


}