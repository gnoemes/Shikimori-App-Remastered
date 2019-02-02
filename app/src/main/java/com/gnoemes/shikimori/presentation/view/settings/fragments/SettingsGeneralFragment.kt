package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.os.Bundle
import androidx.annotation.ArrayRes
import androidx.preference.Preference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.ItemListener
import com.afollestad.materialdialogs.list.listItems
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.utils.firstUpperCase
import com.gnoemes.shikimori.utils.preference
import com.gnoemes.shikimori.utils.prefs
import com.gnoemes.shikimori.utils.putString

class SettingsGeneralFragment : BaseSettingsFragment() {

    private val notSelected by lazy { context!!.getString(R.string.filter_not_selected) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        preference(R.string.settings_anime_player_type_key)?.apply {
            summary = prefs().getString(SettingsExtras.PLAYER_TYPE, PlayerType.EMBEDDED.name)?.let { PlayerType.valueOf(it) }.localizePlayer().firstUpperCase()
            onPreferenceClickListener = playerClickListener
        }

        preference(R.string.settings_anime_translation_type_key)?.apply {
            summary = prefs().getString(SettingsExtras.TRANSLATION_TYPE, notSelected)
                    ?.let { type -> TranslationType.values().find { it.isEqualType(type) }?.localizedType }
                    ?: notSelected
            onPreferenceClickListener = translationClickListener
        }

    }

    override val preferenceScreen: Int
        get() = R.xml.preferences_general

    private val translationClickListener = Preference.OnPreferenceClickListener { preference ->
        showListDialog(R.array.translation_types) { _, index, text ->
            when (index) {
                0 -> prefs().putString(SettingsExtras.TRANSLATION_TYPE, TranslationType.VOICE_RU.type)
                1 -> prefs().putString(SettingsExtras.TRANSLATION_TYPE, TranslationType.SUB_RU.type)
                2 -> prefs().putString(SettingsExtras.TRANSLATION_TYPE, TranslationType.RAW.type)
            }
            preference.summary = text
        }
    }

    private val playerClickListener = Preference.OnPreferenceClickListener { preference ->
        showListDialog(R.array.players) { _, index, text ->
            when (index) {
                0 -> prefs().putString(SettingsExtras.PLAYER_TYPE, PlayerType.WEB.name)
                1 -> prefs().putString(SettingsExtras.PLAYER_TYPE, PlayerType.EMBEDDED.name)
                2 -> prefs().putString(SettingsExtras.PLAYER_TYPE, PlayerType.EXTERNAL.name)
            }
            preference.summary = text
        }
    }

    private fun showListDialog(@ArrayRes items: Int, listener: ItemListener): Boolean {
        MaterialDialog(context!!).show {
            listItems(items, selection = listener)
        }
        return true
    }

    private fun PlayerType?.localizePlayer(): String {
        return when (this) {
            PlayerType.EMBEDDED -> context?.getString(R.string.player_embedded)!!
            PlayerType.EXTERNAL -> context?.getString(R.string.player_external)!!
            PlayerType.WEB -> context?.getString(R.string.player_web)!!
            else -> context?.getString(R.string.player_embedded)!!
        }
    }
}


