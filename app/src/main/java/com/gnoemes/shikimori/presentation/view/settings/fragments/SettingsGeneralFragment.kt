package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import androidx.preference.PreferenceGroup
import androidx.preference.TwoStatePreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.rates.domain.RateSwipeAction
import com.gnoemes.shikimori.utils.preference
import com.gnoemes.shikimori.utils.prefs
import com.gnoemes.shikimori.utils.putString
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File


class SettingsGeneralFragment : BaseSettingsFragment() {

    private val folderChooserLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            val path = getPathFromTreeUri(uri)
            if (path != null) {
                prefs().putString(SettingsExtras.DOWNLOAD_FOLDER, path)
                updateFolderSummary()
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        preference(R.string.settings_content_download_folder_key)?.apply {
            updateFolderSummary()
            setOnPreferenceClickListener { showFolderChooserDialog(); true }
        }

        preference(SettingsExtras.RATE_SWIPE_TO_LEFT_ACTION)?.apply {
            summary = getRateActionSummary(prefs().getString(key, RateSwipeAction.INCREMENT.name)!!)
            setOnPreferenceClickListener { showRateSwipeActionDialog(key); true }
        }

        preference(SettingsExtras.RATE_SWIPE_TO_RIGHT_ACTION)?.apply {
            summary = getRateActionSummary(prefs().getString(key, RateSwipeAction.CHANGE.name)!!)
            setOnPreferenceClickListener { showRateSwipeActionDialog(key); true }
        }

        preference(SettingsExtras.BACKUP_SETTINGS)?.apply {
            setOnPreferenceClickListener {
                BackupDialog().apply {
                    onBackupRestored = { refreshPreferenceSummaries() }
                }.show(childFragmentManager, "BackupDialog")
                true
            }
        }

        val authorized = context?.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
            ?.getLong(SettingsExtras.USER_ID, Constants.NO_ID).let {
                it != null && it != Constants.NO_ID
            }

        preference(SettingsExtras.ALLOW_R18_CONTENT)?.apply {
            isVisible = authorized
        }
    }

    fun refreshPreferenceSummaries() {
        getPreferenceScreen()?.let { syncPreferenceTree(it) }
        updateFolderSummary()
        preference(SettingsExtras.RATE_SWIPE_TO_LEFT_ACTION)?.summary = getRateActionSummary(
            prefs().getString(SettingsExtras.RATE_SWIPE_TO_LEFT_ACTION, RateSwipeAction.INCREMENT.name)!!
        )
        preference(SettingsExtras.RATE_SWIPE_TO_RIGHT_ACTION)?.summary = getRateActionSummary(
            prefs().getString(SettingsExtras.RATE_SWIPE_TO_RIGHT_ACTION, RateSwipeAction.CHANGE.name)!!
        )
    }

    private fun syncPreferenceTree(group: PreferenceGroup) {
        for (i in 0 until group.preferenceCount) {
            val pref = group.getPreference(i)
            when (pref) {
                is PreferenceGroup -> syncPreferenceTree(pref)
                is TwoStatePreference -> {
                    if (pref.key != null) {
                        pref.isChecked = prefs().getBoolean(pref.key, pref.isChecked)
                    }
                }
            }
        }
    }

    private fun getRateActionSummary(action: String): String {
        return when (action) {
            RateSwipeAction.INCREMENT.name -> getString(R.string.rate_swipe_increment)
            RateSwipeAction.CHANGE.name -> getString(R.string.rate_swipe_change)
            RateSwipeAction.ON_HOLD.name -> getString(R.string.rate_swipe_hold)
            RateSwipeAction.DROP.name -> getString(R.string.rate_swipe_drop)
            RateSwipeAction.DISABLED.name -> getString(R.string.rate_swipe_disabled)
            else -> getString(R.string.rate_swipe_disabled)
        }
    }

    override val preferenceScreen: Int
        get() = R.xml.preferences_general

    private fun updateFolderSummary() {
        val folder = prefs().getString(SettingsExtras.DOWNLOAD_FOLDER, "")
        val summary =
            if (!folder.isNullOrEmpty()) folder
            else context!!.getString(R.string.settings_content_download_folder_summary)
        preference(SettingsExtras.DOWNLOAD_FOLDER)?.summary = summary
    }

    private fun showRateSwipeActionDialog(key: String) {
        val items = resources!!.getStringArray(R.array.rate_swipe_actions)
        MaterialAlertDialogBuilder(context!!).apply {
            setItems(items) { _, which ->
                preference(key)?.summary = items[which]
                prefs().putString(key, RateSwipeAction.values()[which].name)
            }
        }.show()
    }

    private fun showFolderChooserDialog() {
        folderChooserLauncher.launch(null)
    }

    private fun getPathFromTreeUri(uri: Uri): String? {
        if (DocumentsContract.isTreeUri(uri)) {
            val docId = DocumentsContract.getTreeDocumentId(uri)
            val parts = docId.split(":")
            if (parts.isNotEmpty() && parts[0] == "primary") {
                return Environment.getExternalStorageDirectory().absolutePath + "/" + parts.drop(1).joinToString(":")
            }
        }
        return null
    }
}


