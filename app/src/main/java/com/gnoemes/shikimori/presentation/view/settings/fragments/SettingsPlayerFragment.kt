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
import com.gnoemes.shikimori.utils.preference
import com.gnoemes.shikimori.utils.prefs
import com.gnoemes.shikimori.utils.putSetting

class SettingsPlayerFragment : BaseSettingsFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)


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

    private fun showNumberEditTextDialog(message : String, prefill: String, callback: InputCallback): Boolean {
        MaterialDialog(context!!).show {
            message(text = message)
            input(prefill = prefill, inputType = InputType.TYPE_CLASS_NUMBER, waitForPositiveButton = false, callback = callback)
            positiveButton(R.string.common_accept)
            negativeButton(R.string.common_cancel)
        }
        return true
    }

    override val preferenceScreen: Int
        get() = R.xml.preferences_player

}