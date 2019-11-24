package com.gnoemes.shikimori.data.repository.series.smotretanime

import android.content.SharedPreferences
import com.gnoemes.shikimori.di.app.annotations.UserQualifier
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import javax.inject.Inject

class Anime365TokenSourceImpl @Inject constructor(
        @UserQualifier private val prefs: SharedPreferences
) : Anime365TokenSource {

    override fun getToken(): String? = prefs.getString(SettingsExtras.ANIME_365_TOKEN, null)
}