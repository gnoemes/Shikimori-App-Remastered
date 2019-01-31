package com.gnoemes.shikimori.data.local.preference.impl

import android.content.SharedPreferences
import com.gnoemes.shikimori.data.local.preference.UserSource
import com.gnoemes.shikimori.di.app.annotations.UserQualifier
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import com.gnoemes.shikimori.utils.putString
import com.gnoemes.shikimori.utils.remove
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import javax.inject.Inject

class UserSourceImpl @Inject constructor(
        @UserQualifier private val prefs: SharedPreferences,
        private val gson: Gson
) : UserSource {

    override fun getUser(): UserBrief? {
        return try {
            val json = prefs.getString(SettingsExtras.USER_BRIEF, "")
            gson.fromJson<UserBrief>(json, UserBrief::class.java)
        } catch (e: JsonSyntaxException) {
            throw IllegalStateException("User doesn't exist")
        }

    }

    override fun setUser(user: UserBrief) {
        prefs.putString(SettingsExtras.USER_BRIEF, gson.toJson(user))
    }

    override fun getUserStatus(): UserStatus {
        val value = prefs.getString(SettingsExtras.USER_STATUS, UserStatus.GUEST.name)!!
        return UserStatus.valueOf(value)
    }

    override fun clearUser() {
        prefs.remove(SettingsExtras.USER_BRIEF)
        setUserStatus(UserStatus.GUEST)
    }

    override fun setUserStatus(status: UserStatus) {
        prefs.putString(SettingsExtras.USER_STATUS, status.name)
    }
}