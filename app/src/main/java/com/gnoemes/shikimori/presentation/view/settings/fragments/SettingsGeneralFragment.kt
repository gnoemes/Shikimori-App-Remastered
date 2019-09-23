package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.Manifest
import android.os.Bundle
import android.os.Environment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.folderChooser
import com.afollestad.materialdialogs.list.listItems
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.rates.domain.RateSwipeAction
import com.gnoemes.shikimori.utils.preference
import com.gnoemes.shikimori.utils.prefs
import com.gnoemes.shikimori.utils.putString
import com.kotlinpermissions.KotlinPermissions
import java.io.File


class SettingsGeneralFragment : BaseSettingsFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        preference(R.string.settings_content_download_folder_key)?.apply {
            updateFolderSummary()
            setOnPreferenceClickListener { checkStoragePermissions { showFolderChooserDialog() };true }
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
                checkStoragePermissions {
                    BackupDialog().show(childFragmentManager, "BackupDialog")
                }
                true
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

    private fun checkStoragePermissions(onAccepted: () -> Unit) {
        KotlinPermissions.with(activity!!)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onAccepted { onAccepted.invoke() }
                .ask()
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
        MaterialDialog(context!!).show {
            listItems(R.array.rate_swipe_actions, waitForPositiveButton = false) { dialog, index, text ->
                preference(key)?.summary = text
                prefs().putString(key, RateSwipeAction.values()[index].name)
            }
        }
    }

    private fun showFolderChooserDialog() {
        val path = prefs().getString(SettingsExtras.DOWNLOAD_FOLDER, "")!!
        val initialDirectory = try {
            File(path).let { if (it.canWrite()) it else Environment.getExternalStorageDirectory() }
        } catch (e: Exception) {
            Environment.getExternalStorageDirectory()
        }

        MaterialDialog(context!!).show {
            folderChooser(
                    initialDirectory = initialDirectory,
                    allowFolderCreation = true,
                    emptyTextRes = R.string.download_folder_empty,
                    folderCreationLabel = R.string.download_new_folder)
            { dialog, file ->
                prefs().putString(SettingsExtras.DOWNLOAD_FOLDER, file.absolutePath)
                updateFolderSummary()
            }
        }
    }
}


