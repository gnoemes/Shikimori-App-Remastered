package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.os.Bundle
import android.text.InputType
import androidx.preference.Preference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.utils.*

class SettingsAnimeFragment : BaseSettingsFragment() {

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

        preference(SettingsExtras.PLAYER_FORWARD_REWIND_OFFSET)?.apply {
            val value = prefs().getLong(key, 10000) / 1000
            summary = String.format(context?.getString(R.string.settings_player_gestures_offset_small_summary_format)!!, value)
            onPreferenceClickListener = smallOffsetClickListener
        }

        preference(SettingsExtras.PLAYER_FORWARD_REWIND_OFFSET_BIG)?.apply {
            val value = prefs().getLong(key, 90000) / 1000
            summary = String.format(context?.getString(R.string.settings_player_gestures_offset_big_summary_format)!!, value)
            onPreferenceClickListener = bigOffsetClickListener
        }

        preference(SettingsExtras.PLAYER_IS_FORWARD_REWIND_SLIDE)?.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) putSetting(SettingsExtras.PLAYER_IS_VOLUME_BRIGHTNESS_GESTURES_ENABLED, false)
                notifyDependencyChange(true)
                return@OnPreferenceChangeListener true
            }
        }
    }

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

    private fun PlayerType?.localizePlayer(): String {
        return when (this) {
            PlayerType.EMBEDDED -> context?.getString(R.string.player_embedded)!!
            PlayerType.EXTERNAL -> context?.getString(R.string.player_external)!!
            PlayerType.WEB -> context?.getString(R.string.player_web)!!
            else -> context?.getString(R.string.player_embedded)!!
        }
    }

    private val smallOffsetClickListener = Preference.OnPreferenceClickListener { preference ->
        val message = context!!.getString(R.string.settings_player_gestures_offset_small_validation_error)
        val prefill = (prefs().getLong(preference.key, 10000) / 1000).toString()
        showNumberEditTextDialog(message, prefill) { dialog: MaterialDialog, text: CharSequence? ->
            val value = text.toString().toIntOrNull()
            val isValid = when (value) {
                in 5..30 -> true
                else -> false
            }

            dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
            dialog.positiveButton {
                if (isValid) {
                    preference.summary = String.format(context?.getString(R.string.settings_player_gestures_offset_small_summary_format)!!, value)
                    putSetting(preference.key, value!! * 1000L)
                }
            }
        }
    }

    private val bigOffsetClickListener = Preference.OnPreferenceClickListener { preference ->
        val message = context!!.getString(R.string.settings_player_gestures_offset_big_validation_error)
        val prefill = (prefs().getLong(preference.key, 90000) / 1000).toString()
        showNumberEditTextDialog(message, prefill) { dialog: MaterialDialog, text: CharSequence? ->
            val value = text.toString().toIntOrNull()
            val isValid = when (value) {
                in 80..180 -> true
                else -> false
            }

            dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
            dialog.positiveButton {
                if (isValid) {
                    preference.summary = String.format(context?.getString(R.string.settings_player_gestures_offset_big_summary_format)!!, value)
                    putSetting(preference.key, value!! * 1000L)
                }
            }
        }
    }

    private fun showNumberEditTextDialog(message: String, prefill: String, callback: InputCallback): Boolean {
        MaterialDialog(context!!).show {
            message(text = message)
            input(prefill = prefill, inputType = InputType.TYPE_CLASS_NUMBER, waitForPositiveButton = false, callback = callback)
            positiveButton(R.string.common_accept)
            negativeButton(R.string.common_cancel)
        }
        return true
    }

    override val preferenceScreen: Int
        get() = R.xml.preferences_anime

}